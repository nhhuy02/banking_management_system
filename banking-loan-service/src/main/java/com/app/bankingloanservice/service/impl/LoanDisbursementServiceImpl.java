package com.app.bankingloanservice.service.impl;

import com.app.bankingloanservice.client.account.AccountClientService;
import com.app.bankingloanservice.client.account.dto.AccountDto;
import com.app.bankingloanservice.client.fundtransfer.FundTransferService;
import com.app.bankingloanservice.client.fundtransfer.dto.FundTransferRequest;
import com.app.bankingloanservice.client.fundtransfer.dto.FundTransferResponse;
import com.app.bankingloanservice.dto.LoanDisbursementResponse;
import com.app.bankingloanservice.dto.kafka.LoanDisbursementNotification;
import com.app.bankingloanservice.entity.Loan;
import com.app.bankingloanservice.exception.*;
import com.app.bankingloanservice.repository.LoanRepository;
import com.app.bankingloanservice.constant.LoanStatus;

import com.app.bankingloanservice.service.LoanDisbursementService;
import com.app.bankingloanservice.service.LoanRepaymentService;
import com.app.bankingloanservice.service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanDisbursementServiceImpl implements LoanDisbursementService {

    private final LoanRepository loanRepository;
    private final LoanService loanService;
    private final AccountClientService accountClientService;
    private final FundTransferService fundTransferService;
    private final LoanRepaymentService loanRepaymentService;
    private final KafkaTemplate<String, LoanDisbursementNotification> loanDisbursementKafkaTemplate;

    private static final String LOAN_DISBURSEMENT_TOPIC = "loan-disbursement-notification";

    @Value("${app.loan.disbursement.source-account-number}")
    private String sourceAccountNumber;

    @Transactional
    @Override
    public LoanDisbursementResponse disburseLoan(Long loanId) {
        // 1. Retrieve the Loan from the DB
        Loan loan = loanService.getLoanEntityById(loanId);

        // 2. Check the Loan status
        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new InvalidLoanException("Loan with ID " + loan.getLoanId() + " is not in PENDING status. Current status: " + loan.getStatus());
        }

        // 3. Fetch the borrower's account information from Account Service
        AccountDto accountInfo = accountClientService.getAccountInfoById(loan.getAccountId());

        // 4. Perform the fund transfer through Transaction Service
        FundTransferRequest fundTransferRequest = FundTransferRequest.builder()
                .fromAccount(sourceAccountNumber)
                .toAccount(accountInfo.getAccountNumber())
                .amount(loan.getLoanAmount())
                .description("Loan disbursement for loan with ID: " + loan.getLoanId())
                .build();
        FundTransferResponse fundTransferResponse = fundTransferService.performFundTransfer(fundTransferRequest);

        // 5. Update the Loan information after successful disbursement
        updateLoanAfterDisbursement(loan);

        // 6. Send notification to the borrower through Notification Service
        sendNotification(loan, accountInfo);

        log.info("Loan with ID {} has been successfully disbursed.", loanId);

        // 7. Create Repayment Schedule
        loanRepaymentService.createRepaymentSchedule(loan);

        // 8. Return LoanDisbursementResponseDto with relevant information
        return LoanDisbursementResponse.builder()
                .loanContractNo(loan.getLoanContractNo())
                .borrowerAccountNumber(accountInfo.getAccountNumber())
                .disbursedAmount(loan.getLoanAmount())
                .transactionReference(fundTransferResponse.getTransactionReference())
                .disbursementDate(LocalDate.now())
                .build();
    }

    private void updateLoanAfterDisbursement(Loan loan) {
        loan.setDisbursementDate(LocalDate.now());
        loan.setStatus(LoanStatus.ACTIVE); // Change status to ACTIVE
        loan.setRemainingBalance(loan.getLoanAmount()); // Remaining balance is the total loan amount
        loan.setTotalPaidAmount(BigDecimal.ZERO); // Initialize total paid amount to 0
        loanRepository.save(loan); // Save the updated Loan information
    }

    private void sendNotification(Loan loan, AccountDto accountInfo) {
        // Prepare notification DTO with loan and borrower information
        LoanDisbursementNotification notification = LoanDisbursementNotification.builder()
                .loanId(loan.getLoanId())
                .customerAccountNumber(accountInfo.getAccountNumber())
                .loanContractNo(loan.getLoanContractNo())
                .customerName(accountInfo.getFullName())
                .customerEmail(accountInfo.getEmail())
                .disbursedAmount(loan.getLoanAmount())
                .disbursementDate(LocalDate.now())
                .build();

        try {
            // Send notification to Kafka using KafkaTemplate
            loanDisbursementKafkaTemplate.send(LOAN_DISBURSEMENT_TOPIC, notification);
            log.info("Loan disbursement notification sent to Account ID {} for loan ID {}", loan.getAccountId(), loan.getLoanId());
        } catch (Exception e) {
            // Log the error but do not throw an exception to avoid impacting the completed disbursement process
            log.error("Error sending loan disbursement notification to Account ID {}: {}", loan.getAccountId(), e.getMessage());
        }
    }
}
