package com.app.bankingloanservice.service.impl;

import com.app.bankingloanservice.client.account.AccountClient;
import com.app.bankingloanservice.client.account.dto.AccountDto;
import com.app.bankingloanservice.client.account.dto.ApiResponse;
import com.app.bankingloanservice.client.fundtransfer.FundTransferClient;
import com.app.bankingloanservice.client.fundtransfer.dto.FundTransferRequest;
import com.app.bankingloanservice.client.fundtransfer.dto.FundTransferResponse;
import com.app.bankingloanservice.dto.LoanRepaymentResponse;
import com.app.bankingloanservice.dto.RepaymentRequest;
import com.app.bankingloanservice.entity.Loan;
import com.app.bankingloanservice.entity.LoanRepayment;
import com.app.bankingloanservice.constant.LoanStatus;
import com.app.bankingloanservice.constant.PaymentStatus;
import com.app.bankingloanservice.exception.*;
import com.app.bankingloanservice.mapper.LoanRepaymentMapper;
import com.app.bankingloanservice.repository.LoanRepaymentRepository;
import com.app.bankingloanservice.repository.LoanRepository;
import com.app.bankingloanservice.service.LoanRepaymentService;
import com.app.bankingloanservice.util.LatePaymentInterestCalculator;
import com.app.bankingloanservice.util.RepaymentCalculator;
import com.app.bankingloanservice.util.RepaymentCalculatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LoanRepaymentServiceImpl implements LoanRepaymentService {

    private final LoanRepaymentRepository loanRepaymentRepository;
    private final LoanRepository loanRepository;
    private final RepaymentCalculatorFactory repaymentCalculatorFactory;
    private final AccountClient accountClient;
    private final FundTransferClient fundTransferClient;
    private final LoanRepaymentMapper loanRepaymentMapper;

    @Value("${app.loan.repayment.collection-account-number}")
    private String collectionAccountNumber; // Bank's loan collection account

    @Override
    public void createRepaymentSchedule(Loan loan) {
        RepaymentCalculator calculator = repaymentCalculatorFactory.getRepaymentCalculator(loan.getRepaymentMethod());
        calculator.calculateRepaymentSchedule(loan);
    }

    @Override
    @Transactional
    public void makeRepayment(Long loanId, Long repaymentId, RepaymentRequest repaymentRequest) {
        // 1. Retrieve and validate the loan
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan with ID " + loanId + " not found"));

        if (loan.getStatus() != LoanStatus.ACTIVE) {
            throw new InvalidLoanException("Loan is not active for repayment.");
        }

        // 2. Retrieve and validate the repayment schedule
        LoanRepayment repayment = loanRepaymentRepository.findById(repaymentId)
                .orElseThrow(() -> new RepaymentNotFoundException("Repayment with ID " + repaymentId + " not found"));

        if (!repayment.getLoan().getLoanId().equals(loanId)) {
            throw new InvalidRepaymentException("Repayment does not belong to the specified loan.");
        }

        if (repayment.getPaymentStatus() == PaymentStatus.PAID) {
            throw new InvalidRepaymentException("This repayment has already been paid.");
        }

        // 3. Check and calculate late payment interest
        boolean isLate = LatePaymentInterestCalculator.isLate(repayment);
        BigDecimal latePaymentInterest = BigDecimal.ZERO;
        if (isLate) {
            latePaymentInterest = LatePaymentInterestCalculator.calculateLatePaymentInterest(loan, repayment);
            repayment.setLatePaymentInterestAmount(latePaymentInterest);
        }

        // 4. Calculate the total amount due
        BigDecimal totalAmountDue = repayment.getTotalAmount();

        // 5. Retrieve customer account information from Account Service
        AccountDto borrowerAccount = getAccountInfo(repaymentRequest.getAccountId());

        // 6. Perform fund transfer from customer account to loan collection account
        FundTransferRequest fundTransferRequest = FundTransferRequest.builder()
                .fromAccount(borrowerAccount.getAccountNumber()) // Customer's account
                .toAccount(collectionAccountNumber) // Bank's loan collection account
                .amount(totalAmountDue)
                .description("Loan Repayment for Loan ID: " + loanId + ", Repayment ID: " + repaymentId)
                .build();

        FundTransferResponse fundTransferResponse = performFundTransfer(fundTransferRequest);

        // 7. Validate fund transfer result
        if (!"SUCCESS".equalsIgnoreCase(fundTransferResponse.getMessage())) {
            throw new FundTransferException("Fund transfer failed: " + fundTransferResponse.getMessage());
        }

        // 8. Update repayment information
        repayment.setActualPaymentDate(LocalDate.now());
        repayment.setPaymentStatus(PaymentStatus.PAID);
        repayment.setIsLate(isLate);
        loanRepaymentRepository.save(repayment);

        // 9. Update loan information
        loan.setTotalPaidAmount(loan.getTotalPaidAmount().add(totalAmountDue));
        loan.setRemainingBalance(loan.getRemainingBalance().subtract(repayment.getPrincipalAmount()));
        loanRepository.save(loan);

        // 10. Check and update loan settlement status
        if (loan.getRemainingBalance().compareTo(BigDecimal.ZERO) <= 0) {
            loan.setSettlementDate(LocalDate.now());
            if (isLate) {
                loan.setStatus(LoanStatus.SETTLED_LATE);
            } else if (repayment.getPaymentDueDate().isAfter(LocalDate.now())) {
                loan.setStatus(LoanStatus.SETTLED_EARLY);
            } else {
                loan.setStatus(LoanStatus.SETTLED_ON_TIME);
            }
            loanRepository.save(loan);
        }
    }

    @Override
    public Page<LoanRepaymentResponse> getRepaymentSchedule(Long loanId, Pageable pageable) {
        Page<LoanRepayment> repayments = loanRepaymentRepository.findByLoanLoanId(loanId, pageable);
        return repayments.map(loanRepaymentMapper::toResponse);
    }

    /**
     * Retrieve customer account information from Account Service
     *
     * @param accountId ID of the customer's account
     * @return Account information
     */
    private AccountDto getAccountInfo(Long accountId) {
        try {
            ApiResponse<AccountDto> apiResponse = accountClient.getAccountById(accountId);
            if (!apiResponse.isSuccess()) {
                throw new ExternalServiceException("Unable to retrieve account information from Account Service: " + apiResponse.getMessage());
            }
            return apiResponse.getData();
        } catch (Exception e) {
            throw new ExternalServiceException("Error calling Account Service for Account ID: " + accountId, e);
        }
    }

    /**
     * Perform fund transfer via FundTransferClient
     *
     * @param fundTransferRequest Fund transfer request
     * @return Response from Fund Transfer Service
     */
    private FundTransferResponse performFundTransfer(FundTransferRequest fundTransferRequest) {
        try {
            FundTransferResponse response = fundTransferClient.transferFunds(fundTransferRequest);
            if (response == null || response.getTransactionReference() == null) {
                throw new FundTransferException("Repayment transfer failed for Loan ID " + fundTransferRequest.getDescription());
            }
            // Additional checks if necessary, e.g., status in response
            return response;
        } catch (Exception e) {
            throw new FundTransferException("Error performing fund transfer for repayment: " + fundTransferRequest.getDescription(), e);
        }
    }
}
