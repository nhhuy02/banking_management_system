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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
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
     * @param loanId      ID of the loan
     * @param repaymentId ID of the repayment schedule
     * @return LoanRepaymentResponse containing repayment details and transaction reference
     */
    @Transactional
    @Override
    public LoanRepaymentResponse makeRepayment(Long loanId, Long repaymentId) {
        // 1. Retrieve and validate the loan
        Loan loan = loanService.getLoanEntityById(loanId);
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

        // 3. Check if the repayment is for the current period
        LocalDate now = LocalDate.now();
        LocalDate paymentDueDate = repayment.getPaymentDueDate();

        // Check if the repayment due date is for the next month
        if (now.isBefore(paymentDueDate.minusMonths(1).plusDays(1))) {
            throw new InvalidRepaymentException("Repayment can only be made for the current period.");
        }

        // 4. Check and calculate late payment interest
        boolean isLate = now.isAfter(paymentDueDate);
        BigDecimal latePaymentInterest = BigDecimal.ZERO;
        if (isLate) {
            latePaymentInterest = LatePaymentInterestCalculator.calculateLatePaymentInterest(loan, repayment);
            repayment.setLatePaymentInterestAmount(latePaymentInterest);
            // Update late payment status and save
            repayment.setPaymentStatus(PaymentStatus.OVERDUE);
            loanRepaymentRepository.save(repayment);
        }

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
        LoanRepaymentResponse repaymentResponse = loanRepaymentMapper.toResponse(repayment);
        repaymentResponse.setTransactionReference(fundTransferResponse.getTransactionReference());
        repaymentResponse.setAccountNumber(borrowerAccount.getAccountNumber());
        repaymentResponse.setCustomerFullName(borrowerAccount.getFullName());

        return repaymentResponse;
    }


    /**
     * Retrieves the repayment schedule for a loan, with pagination and sorting.
     * Handles sorting direction and any exceptions related to invalid direction values.
     *
     * @param loanId    The ID of the loan.
     * @param page      The current page number.
     * @param size      The page size.
     * @param sortBy    The field to sort by.
     * @param direction The sorting direction (asc/desc).
     * @return Page of LoanRepaymentResponse.
     */
    @Override
    public Page<LoanRepaymentResponse> getRepaymentSchedule(Long loanId, int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection;

        // Try to convert the direction string to Sort.Direction
        try {
            sortDirection = Sort.Direction.fromString(direction); // Handles both uppercase and lowercase
        } catch (IllegalArgumentException e) {
            // Log and throw exception if the sort direction is invalid
            throw new IllegalArgumentException("Invalid sort direction value: " + direction);
        }

        // Build pageable with sorting
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        // Fetch loan repayments with pagination
        Page<LoanRepayment> repayments = loanRepaymentRepository.findByLoanLoanId(loanId, pageable);

        // Get Account information and map to response:
        Loan loan = loanService.getLoanEntityById(loanId);
        AccountDto accountInfo = accountClientService.getAccountInfoById(loan.getAccountId());
        return repayments.map(repayment -> {
            LoanRepaymentResponse repaymentResponse = loanRepaymentMapper.toResponse(repayment);
            repaymentResponse.setAccountNumber(accountInfo.getAccountNumber());
            repaymentResponse.setCustomerFullName(accountInfo.getFullName());
            return repaymentResponse;
        });
    }

}