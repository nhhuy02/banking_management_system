package com.app.bankingloanservice.service.impl;

import com.app.bankingloanservice.client.account.AccountClient;
import com.app.bankingloanservice.client.account.dto.AccountDto;
import com.app.bankingloanservice.client.account.dto.ApiResponse;
import com.app.bankingloanservice.client.fundtransfer.FundTransferClient;
import com.app.bankingloanservice.client.fundtransfer.dto.FundTransferRequest;
import com.app.bankingloanservice.client.fundtransfer.dto.FundTransferResponse;
import com.app.bankingloanservice.client.notification.NotificationClient;
import com.app.bankingloanservice.client.notification.dto.NotificationRequest;
import com.app.bankingloanservice.entity.Loan;
import com.app.bankingloanservice.exception.ExternalServiceException;
import com.app.bankingloanservice.exception.InvalidLoanException;
import com.app.bankingloanservice.exception.LoanNotFoundException;
import com.app.bankingloanservice.exception.TransactionFailedException;
import com.app.bankingloanservice.repository.LoanRepository;
import com.app.bankingloanservice.constant.LoanStatus;

import com.app.bankingloanservice.service.LoanDisbursementService;
import com.app.bankingloanservice.service.LoanRepaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanDisbursementServiceImpl implements LoanDisbursementService {

    private final LoanRepository loanRepository;
    private final AccountClient accountClient;
    private final FundTransferClient fundTransferClient;
    private final NotificationClient notificationClient;
    private final LoanRepaymentService loanRepaymentService;

    @Value("${app.loan.disbursement.source-account-number}")
    private String sourceAccountNumber;

    @Transactional
    @Override
    public void disburseLoan(Long loanId) {
        // 1. Retrieve the Loan from the DB
        Loan loan = getLoanEntityById(loanId);

        // 2. Check the Loan status
        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new InvalidLoanException("Loan is not in PENDING status");
        }

        // 3. Fetch the borrower's account information from Account Service
        AccountDto accountInfo = getAccountInfo(loan.getAccountId());

        // 4. Perform the fund transfer through Transaction Service
        FundTransferResponse fundTransferResponse = performFundTransfer(loan, accountInfo);

        // 5. Update the Loan information after successful disbursement
        updateLoanAfterDisbursement(loan);

        // 6. Create Repayment Schedule
        loanRepaymentService.createRepaymentSchedule(loan);

        // 7. Send notification to the borrower through Notification Service
        sendNotification(loan);

        log.info("Loan with ID {} has been successfully disbursed.", loanId);

    }

    private Loan getLoanEntityById(Long loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan with ID " + loanId + " not found"));
    }

    private AccountDto getAccountInfo(Long accountId) {
        try {
            ApiResponse<AccountDto> apiResponse = accountClient.getAccountById(accountId);
            if (!apiResponse.isSuccess()) {
                throw new ExternalServiceException("Unable to retrieve account information from Account Service: " + apiResponse.getMessage());
            }
            return apiResponse.getData();
        } catch (Exception e) {
            throw new ExternalServiceException("Error calling Account Service", e);
        }
    }

    private FundTransferResponse performFundTransfer(Loan loan, AccountDto accountInfo) {
        FundTransferRequest fundTransferRequest = FundTransferRequest.builder()
                .fromAccount(sourceAccountNumber) // Source account retrieved from configuration
                .toAccount(accountInfo.getAccountNumber()) // Borrower's account number
                .amount(loan.getLoanAmount()) // Disbursed loan amount
                .description("Disbursement for loan contract " + loan.getLoanContractNo())
                .build();

        try {
            FundTransferResponse response = fundTransferClient.transferFunds(fundTransferRequest);
            if (response == null || response.getTransactionReference() == null) {
                throw new TransactionFailedException("Disbursement transfer failed: No valid response from Transaction Service");
            }
            // You can add additional checks if necessary, such as checking status in the response
            return response;
        } catch (Exception e) {
            throw new ExternalServiceException("Error calling Transaction Service", e);
        }
    }

    private void updateLoanAfterDisbursement(Loan loan) {
        loan.setDisbursementDate(LocalDate.now());
        loan.setStatus(LoanStatus.ACTIVE); // Change status to ACTIVE
        loan.setRemainingBalance(loan.getLoanAmount()); // Remaining balance is the total loan amount
        loan.setTotalPaidAmount(BigDecimal.ZERO); // Initialize total paid amount to 0
        loanRepository.save(loan); // Save the updated Loan information
    }

    private void sendNotification(Loan loan) {
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .accountId(loan.getAccountId())
                .message("Your loan of " + loan.getLoanAmount() + " VND has been disbursed.")
                .build();

        try {
            notificationClient.sendNotification(notificationRequest);
            log.info("Notification sent to Account ID {}", loan.getAccountId());
        } catch (Exception e) {
            // Log the error but do not throw an exception to avoid impacting the completed disbursement process
            log.error("Error sending notification to Account ID {}: {}", loan.getAccountId(), e.getMessage());
        }
    }
}
