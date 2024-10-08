package com.app.bankingloanservice.service.impl;

import com.app.bankingloanservice.client.account.AccountClientService;
import com.app.bankingloanservice.client.account.dto.AccountDto;
import com.app.bankingloanservice.client.fundtransfer.FundTransferService;
import com.app.bankingloanservice.client.fundtransfer.dto.FundTransferRequest;
import com.app.bankingloanservice.client.fundtransfer.dto.FundTransferResponse;
import com.app.bankingloanservice.dto.LoanRepaymentResponse;
import com.app.bankingloanservice.entity.Loan;
import com.app.bankingloanservice.entity.LoanRepayment;
import com.app.bankingloanservice.constant.LoanStatus;
import com.app.bankingloanservice.constant.PaymentStatus;
import com.app.bankingloanservice.exception.*;
import com.app.bankingloanservice.mapper.LoanRepaymentMapper;
import com.app.bankingloanservice.repository.LoanRepaymentRepository;
import com.app.bankingloanservice.repository.LoanRepository;
import com.app.bankingloanservice.service.LoanRepaymentService;
import com.app.bankingloanservice.service.LoanService;
import com.app.bankingloanservice.util.LatePaymentInterestCalculator;
import com.app.bankingloanservice.util.RepaymentCalculator;
import com.app.bankingloanservice.util.RepaymentCalculatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LoanRepaymentServiceImpl implements LoanRepaymentService {

    private final LoanRepaymentRepository loanRepaymentRepository;
    private final LoanService loanService;
    private final LoanRepository loanRepository;
    private final RepaymentCalculatorFactory repaymentCalculatorFactory;
    private final AccountClientService accountClientService;
    private final FundTransferService fundTransferService;
    private final LoanRepaymentMapper loanRepaymentMapper;

    @Value("${app.loan.repayment.collection-account-number}")
    private String collectionAccountNumber; // Bank's loan collection account

    @Override
    public void createRepaymentSchedule(Loan loan) {
        RepaymentCalculator calculator = repaymentCalculatorFactory.getRepaymentCalculator(loan.getRepaymentMethod());
        calculator.calculateRepaymentSchedule(loan);
    }

    /**
     * Make a repayment and return the repayment response including transaction reference.
     *
     * @param repaymentId ID of the repayment schedule
     * @return LoanRepaymentResponse containing repayment details and transaction reference
     */
    @Override
    public LoanRepaymentResponse makeRepayment(Long repaymentId) {

        // 1. Retrieve and validate the repayment schedule
        LoanRepayment repayment = loanRepaymentRepository.findById(repaymentId)
                .orElseThrow(() -> new RepaymentNotFoundException("Repayment with ID " + repaymentId + " not found"));

        if (repayment.getPaymentStatus() == PaymentStatus.PAID) {
            throw new InvalidRepaymentException("This repayment has already been paid.");
        }

        // 2. Retrieve and validate the loan
        Long loanId = repayment.getLoan().getLoanId();
        Loan loan = loanService.getLoanEntityById(loanId);
        if (loan.getStatus() != LoanStatus.ACTIVE) {
            throw new InvalidLoanException("Loan is not active for repayment.");
        }

        // 3. Check if the repayment is for the current period
        LocalDate now = LocalDate.now();
        LocalDate paymentDueDate = repayment.getPaymentDueDate();

        // Check if repayment is within a valid payment period
        if (!isWithinPaymentWindow(repayment, now)) {
            throw new InvalidRepaymentException("Repayment can only be made for the current period.");
        }

        // 4. Check and calculate late payment interest
        boolean isLate = now.isAfter(paymentDueDate);
        BigDecimal latePaymentInterest = BigDecimal.ZERO;
        if (isLate) {
            latePaymentInterest = LatePaymentInterestCalculator.calculateLatePaymentInterest(loan, repayment);
            // Update late payment status and save
            repayment.setPaymentStatus(PaymentStatus.OVERDUE);
            loanRepaymentRepository.save(repayment);
        }
        repayment.setLatePaymentInterestAmount(latePaymentInterest);

        // 5. Calculate the total amount due
        BigDecimal totalAmountDue = repayment.getTotalAmount();

        // 6. Retrieve customer account information from Account Service
        AccountDto borrowerAccount = accountClientService.getAccountInfoById(loan.getAccountId());

        // 7. Perform fund transfer from customer account to loan collection account
        FundTransferRequest fundTransferRequest = FundTransferRequest.builder()
                .fromAccount(borrowerAccount.getAccountNumber()) // Customer's account
                .toAccount(collectionAccountNumber) // Bank's loan collection account
                .amount(totalAmountDue)
                .description("Loan Repayment for Loan ID: " + loanId + ", Repayment ID: " + repaymentId)
                .build();
        FundTransferResponse fundTransferResponse = fundTransferService.performFundTransfer(fundTransferRequest);

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
            } else if (paymentDueDate.isAfter(LocalDate.now())) {
                loan.setStatus(LoanStatus.SETTLED_EARLY);
            } else {
                loan.setStatus(LoanStatus.SETTLED_ON_TIME);
            }
            loanRepository.save(loan);
        }

        // 11. Map repayment to response and include transaction reference, account info
        LoanRepaymentResponse repaymentResponse = mapToResponse(repayment, borrowerAccount);
        repaymentResponse.setTransactionReference(fundTransferResponse.getTransactionReference());

        return repaymentResponse;
    }


    /**
     * Retrieves the repayment schedule for a loan, ordered by paymentDueDate.
     *
     * @param loanId The ID of the loan.
     * @return List of LoanRepaymentResponse.
     */
    @Override
    @Transactional(readOnly = true)
    public List<LoanRepaymentResponse> getRepaymentSchedule(Long loanId) {

        List<LoanRepayment> repayments;
        try {
            // Get all payments by loanId and sort by paymentDueDate
            repayments = loanRepaymentRepository.findByLoanLoanIdOrderByPaymentDueDateAsc(loanId);
        } catch (Exception e) {
            log.error("Error fetching loan repayments for loan ID: {}", loanId, e);
            throw new LoanRepaymentFetchException("Failed to fetch loan repayments", e);
        }

        // Get account information and map to LoanRepaymentResponse
        Loan loan = loanService.getLoanEntityById(loanId);
        AccountDto accountInfo = accountClientService.getAccountInfoById(loan.getAccountId());

        return repayments.stream()
                .map(repayment -> mapToResponse(repayment, accountInfo))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<LoanRepaymentResponse> getRepaymentsByAccountIdAndStatus(Long accountId, PaymentStatus paymentStatus) {
        log.info("Fetching loan repayments for account ID: {} with payment status: {}", accountId, paymentStatus);

        // Validate input accountId
        if (accountId == null) {
            log.error("Failed to fetch loan repayments: Account ID cannot be null");
            throw new IllegalArgumentException("Account ID cannot be null.");
        }

        // Query loan repayments by accountId and optional paymentStatus
        log.info("Querying loan repayments for account ID: {} with payment status: {}", accountId, paymentStatus);
        List<LoanRepayment> repayments = paymentStatus != null
                ? loanRepaymentRepository.findByAccountIdAndPaymentStatus(accountId, paymentStatus)
                : loanRepaymentRepository.findByAccountId(accountId);

        // Get account information from Account Service
        AccountDto accountInfo = accountClientService.getAccountInfoById(accountId);

        // Map LoanRepayments to LoanRepaymentResponses
        log.info("Mapping loan repayments to LoanRepaymentResponse DTOs for account ID: {}", accountId);
        return repayments.stream()
                .map(repayment -> mapToResponse(repayment, accountInfo))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanRepaymentResponse> getAvailableLoanRepayments(Long accountId) {
        log.info("Fetching available loan repayments for account ID: {}", accountId);

        // Ensure accountId is not null to avoid invalid operations
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null.");
        }

        // Fetch current date for comparing payment windows
        LocalDate now = LocalDate.now();

        // Calculate payment cutoff date (current date + 1 month)
        LocalDate paymentCutOffDate = now.plusMonths(1);

        // Fetch loan repayments that are eligible for payment
        List<LoanRepayment> repayments = fetchEligibleRepayments(accountId, paymentCutOffDate);

        // Fetch account information from external service
        AccountDto accountInfo = accountClientService.getAccountInfoById(accountId);

        // Map to response object and sort by paymentDueDate in ascending order
        List<LoanRepaymentResponse> availableRepayments = repayments.stream()
                .sorted(Comparator.comparing(LoanRepayment::getPaymentDueDate)) // Sort by paymentDueDate
                .map(repayment -> mapToResponse(repayment, accountInfo))
                .toList();

        // Log the number of available repayments found
        log.info("Found {} available loan repayments for account ID: {}", availableRepayments.size(), accountId);

        return availableRepayments;
    }

    private List<LoanRepayment> fetchEligibleRepayments(Long accountId, LocalDate paymentCutOffDate) {
        try {
            // Retrieve repayments that are eligible for payment
            return loanRepaymentRepository.findByAccountIdAndWithinPaymentWindowAndUnpaid(
                    accountId,
                    LoanStatus.ACTIVE,
                    PaymentStatus.PAID,
                    paymentCutOffDate
            );
        } catch (Exception e) {
            // Log and wrap exception to provide more context
            log.error("Error fetching eligible loan repayments for account ID: {}", accountId, e);
            throw new LoanRepaymentFetchException("Failed to fetch loan repayments", e);
        }
    }


    /**
     * Checks if the repayment is within the valid payment window.
     *
     * @param repayment the LoanRepayment object
     * @param now       the current date
     * @return true if the repayment is within the allowed payment window
     */
    private boolean isWithinPaymentWindow(LoanRepayment repayment, LocalDate now) {
        // Check if the current date is after the allowed window (1 month before due date)
        // (paymentDueDate - 30) < now <=> paymentDueDate < (now + 30)
        return now.isAfter(repayment.getPaymentDueDate().minusMonths(1));
    }

    /**
     * Map LoanRepayment entity to LoanRepaymentResponse DTO.
     *
     * @param repayment   the LoanRepayment entity
     * @param accountInfo the account information associated with the repayment
     * @return the mapped LoanRepaymentResponse object
     */
    private LoanRepaymentResponse mapToResponse(LoanRepayment repayment, AccountDto accountInfo) {
        LoanRepaymentResponse response = loanRepaymentMapper.toResponse(repayment);
        // Set additional account information from the external service
        response.setAccountNumber(accountInfo.getAccountNumber());
        response.setCustomerFullName(accountInfo.getFullName());
        return response;
    }

}