package com.app.bankingloanservice.service.impl;

import com.app.bankingloanservice.client.account.AccountClientService;
import com.app.bankingloanservice.client.account.dto.AccountDto;
import com.app.bankingloanservice.constant.ApplicationStatus;
import com.app.bankingloanservice.dto.LoanInterestRateRequest;
import com.app.bankingloanservice.dto.LoanRequest;
import com.app.bankingloanservice.dto.LoanResponse;
import com.app.bankingloanservice.entity.*;
import com.app.bankingloanservice.exception.LoanCreationException;
import com.app.bankingloanservice.exception.LoanNotFoundException;
import com.app.bankingloanservice.mapper.LoanMapper;
import com.app.bankingloanservice.mapper.LoanTypeMapper;
import com.app.bankingloanservice.repository.CollateralRepository;
import com.app.bankingloanservice.repository.LoanRepository;
import com.app.bankingloanservice.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final LoanTypeMapper loanTypeMapper;
    private final LoanInterestRateService loanInterestRateService;
    private final LoanApplicationService loanApplicationService;
    private final LoanTypeService loanTypeService;
    private final CollateralRepository collateralRepository;
    private final AccountClientService accountClientService;


    /**
     * Creates a new loan from a loan application and saves it in the database.
     *
     * @param loanApplicationId the loan application ID to convert into a loan
     * @return the created loan entity
     */
    @Override
    public LoanResponse createLoanFromApplicationId(Long loanApplicationId) {

        // Get Loan Application to convert to Loan
        LoanApplication loanApplication = loanApplicationService.getEntityById(loanApplicationId);

        log.info("Creating loan from application ID: {}", loanApplicationId);

        // Check if the loan application has been approved
        if (loanApplication.getApplicationStatus() != ApplicationStatus.APPROVED) {
            throw new LoanCreationException("Loan application must be approved before creating a loan.");
        }

        // Convert LoanApplication to Loan using LoanMapper
        LocalDate maturityDate = calculateMaturityDate(loanApplication.getDesiredDisbursementDate(), loanApplication.getDesiredLoanTermMonths());
        Loan newLoan = loanMapper.loanApplicationToLoan(loanApplication, generateUniqueLoanContractNo(), maturityDate);
        newLoan.setLoanApplication(loanApplication); // Link Loan with LoanApplication

        try {
            // Save the Loan entity to the database
            newLoan = loanRepository.save(newLoan);

            // Convert LoanType to LoanInterestRateRequest using LoanTypeMapper
            LoanInterestRateRequest loanInterestRateRequest = loanTypeMapper.toLoanInterestRateCreateDto(loanApplication.getLoanType());
            loanInterestRateRequest.setEffectiveFrom(newLoan.getDisbursementDate()); // Set the effective date

            // Save Loan Interest Rate using LoanInterestRateService
            LoanInterestRate loanInterestRate = loanInterestRateService.createLoanInterestRate(newLoan.getLoanId(), loanInterestRateRequest);

            // Assign LoanInterestRate to the current Loan
            newLoan.setCurrentInterestRate(loanInterestRate);

            // Save the Loan again after setting all necessary fields
            newLoan = loanRepository.save(newLoan);

            // Update collateral with associated Loan
            Collateral collateral = loanApplication.getCollateral();
            collateral.setLoan(newLoan);
            collateralRepository.save(collateral);

            log.info("Loan created successfully with contract number: {}", newLoan.getLoanContractNo());

            // Map to LoanResponse and return
            return generateLoanResponse(newLoan);
        } catch (Exception e) {
            // Log error with detailed information and throw LoanCreationException
            String errorMessage = String.format("Error occurred while creating loan from application with ID: %s", loanApplication.getLoanApplicationId());
            log.error(errorMessage, e); // Log the error and original exception
            throw new LoanCreationException(errorMessage, e); // Throw custom exception with message and cause
        }
    }


    @Override
    public LoanResponse createLoan(LoanRequest loanRequest) {

        // Convert LoanRequest to Loan using LoanMapper
        LocalDate maturityDate = calculateMaturityDate(loanRequest.getDisbursementDate(), loanRequest.getLoanTermMonths());
        LoanApplication loanApplication = loanApplicationService.getEntityById(loanRequest.getLoanApplicationId());
        LoanType loanType = loanTypeService.getLoanTypeById(loanRequest.getLoanTypeId());
        Loan newLoan = loanMapper.toEntity(loanRequest, loanApplication, generateUniqueLoanContractNo(), maturityDate, loanType);

        try {
            // Save the Loan entity to the database
            newLoan = loanRepository.save(newLoan);

            log.info("Just saved loan to database for the first time, loan ID: {}", newLoan.getLoanId());

            // Assign LoanInterestRate to the current Loan
            LoanInterestRate loanInterestRate = loanInterestRateService.createLoanInterestRate(newLoan.getLoanId(), loanRequest.getCurrentInterestRate());
            newLoan.setCurrentInterestRate(loanInterestRate);

            // Save the Loan again after setting all necessary fields
            newLoan = loanRepository.save(newLoan);

            log.info("Loan created successfully with contract number: {}", newLoan.getLoanContractNo());
            return generateLoanResponse(newLoan);
        } catch (Exception e) {
            // Log error with detailed information and throw LoanCreationException
            String errorMessage = String.format("Error occurred while creating loan with application ID: %s", loanApplication.getLoanApplicationId());
            log.error(errorMessage, e); // Log the error and original exception
            throw new LoanCreationException(errorMessage, e); // Throw custom exception with message and cause
        }
    }


    /**
     * Retrieves a loan by its ID. Throws LoanNotFoundException if the loan is not found.
     *
     * @param loanId the ID of the loan to retrieve
     * @return the loan entity
     * @throws LoanNotFoundException if no loan is found with the given ID
     */
    @Transactional(readOnly = true)
    @Override
    public Loan getLoanEntityById(Long loanId) {
        log.info("Attempting to get loan with ID: {}", loanId);

        return loanRepository.findById(loanId).orElseThrow(() -> {
            String errorMessage = String.format("Loan with ID %d not found. Please check the ID and try again.", loanId);
            log.error(errorMessage);
            return new LoanNotFoundException(errorMessage);
        });
    }

    @Override
    public LoanResponse getLoanResponseDtoById(Long loanId) {
        return generateLoanResponse(getLoanEntityById(loanId));
    }


    @Override
    public List<LoanResponse> getLoansByAccountId(Long accountId) {

        log.info("Fetching loans for account ID: {}", accountId);

        List<Loan> loanList = loanRepository.findByAccountId(accountId);

        if (loanList.isEmpty()) {
            log.warn("No loans found for account ID: {}", accountId);
            return Collections.emptyList();
        }

        // Map Loan to LoanResponse
        List<LoanResponse> responseDtos = loanList.stream()
                .map(this::generateLoanResponse)
                .collect(Collectors.toList());

        log.info("Fetched {} loans for account ID: {}", responseDtos.size(), accountId);

        return responseDtos;
    }


    // Method to generate contract number
    private String generateUniqueLoanContractNo() {
        String contractNo;
        int maxAttempts = 5;
        int attempt = 0;

        do {
            contractNo = "BNK-LN-" + LocalDate.now().getYear() + "-"
                    + String.format("%02d", LocalDate.now().getMonthValue()) + "-"
                    + String.format("%05d", new Random().nextInt(100000));

            boolean exists = loanRepository.existsByLoanContractNo(contractNo);
            if (!exists) {
                return contractNo;
            }

            attempt++;
        } while (attempt < maxAttempts);

        throw new LoanCreationException("Unable to generate unique loan contract number after " + maxAttempts + " attempts.");
    }

    // Calculate maturity date based on disbursement date and loan term
    private LocalDate calculateMaturityDate(LocalDate disbursementDate, int loanTerm) {
        return disbursementDate.plusMonths(loanTerm);
    }

    // Add Account information from Account Service:
    private LoanResponse generateLoanResponse(Loan loan) {

        // Map Loan to response:
        LoanResponse loanResponse = loanMapper.toResponse(loan);

        // Get Account info from Account Service:
        AccountDto accountInfo = accountClientService.getAccountInfoById(loan.getAccountId());

        // Set Account info:
        loanResponse.setCustomerName(accountInfo.getFullName());
        loanResponse.setContactPhone(accountInfo.getPhoneNumber());
        loanResponse.setContactEmail(accountInfo.getEmail());
        loanResponse.setAccountNumber(accountInfo.getAccountNumber());

        return loanResponse;
    }

}
