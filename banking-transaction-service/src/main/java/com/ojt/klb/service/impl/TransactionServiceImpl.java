package com.ojt.klb.service.impl;

import com.ojt.klb.exception.GlobalErrorCode;
import com.ojt.klb.exception.InsufficientBalance;
import com.ojt.klb.exception.ResourceNotFound;
import com.ojt.klb.exception.TransactionFailedException;
import com.ojt.klb.external.AccountService;
import com.ojt.klb.model.TransactionStatus;
import com.ojt.klb.model.TransactionType;
import com.ojt.klb.model.dto.TransactionDto;
import com.ojt.klb.model.dto.TransactionNotificationDto;
import com.ojt.klb.model.entity.Transaction;
import com.ojt.klb.model.external.Account;
import com.ojt.klb.model.mapper.TransactionMapper;
import com.ojt.klb.model.response.Response;
import com.ojt.klb.repository.TransactionRepository;
import com.ojt.klb.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.ojt.klb.model.TransactionStatus.COMPLETED;
import static com.ojt.klb.model.TransactionType.INTERNAL_TRANSFER;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository repository;
    private final AccountService accountService;
    private final TransactionMapper mapper = new TransactionMapper();

    @Transactional
    @Override
    public Response handleDeposit(String accountNumber, BigDecimal amount) {
        Account account = getAndValidateAccount(accountNumber);
        String referenceNumber = generateUniqueReferenceNumber();
        validateBalance(account, amount);

        TransactionDto internalTransferDto = TransactionDto.builder()
                .referenceNumber(referenceNumber)
                .accountId(account.getAccountId())
                .transactionType(TransactionType.DEPOSIT.name())
                .amount(amount)
                .transactionDate(LocalDateTime.now())
                .status(COMPLETED.name())
                .build();

        Transaction transaction = mapper.convertToEntity(internalTransferDto);
        repository.save(transaction);

        try {
            account.setAvailableBalance(account.getAvailableBalance().add(amount));
            repository.save(transaction);
            accountService.updateAccount(account.getAccountId(), account);

            return Response.builder()
                    .message("Deposit completed successfully")
                    .build();

        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.FAILED);
            repository.save(transaction);

            log.error("Deposit failed for account {}: {}", accountNumber, e.getMessage(), e);

            throw new TransactionFailedException("Deposit failed: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public Response handleWithdraw(String accountNumber, BigDecimal amount) {
        Account account = getAndValidateAccount(accountNumber);
        String referenceNumber = generateUniqueReferenceNumber();
        validateBalance(account, amount);

        TransactionDto internalTransferDto = TransactionDto.builder()
                .referenceNumber(referenceNumber)
                .accountId(account.getAccountId())
                .transactionType(TransactionType.WITHDRAWAL.name())
                .amount(amount)
                .transactionDate(LocalDateTime.now())
                .status(COMPLETED.name())
                .build();

        Transaction transaction = mapper.convertToEntity(internalTransferDto);
        repository.save(transaction);

        try {
            account.setAvailableBalance(account.getAvailableBalance().subtract(amount));
            repository.save(transaction);
            accountService.updateAccount(account.getAccountId(), account);

            return Response.builder()
                    .message("Withdraw completed successfully")
                    .build();

        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.FAILED);
            repository.save(transaction);

            log.error("Withdraw failed for account {}: {}", accountNumber, e.getMessage(), e);

            throw new TransactionFailedException("Withdraw failed: " + e.getMessage());
        }
    }

    private void validateBalance(Account account, BigDecimal amount) {
        if (account.getAvailableBalance().compareTo(BigDecimal.ZERO) < 0 || account.getAvailableBalance().compareTo(amount) < 0) {
            throw new InsufficientBalance("Insufficient funds in the account");
        }
    }

    private Account getAndValidateAccount(String accountNumber) {
        return accountService.readByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFound("Requested account not found on the server", GlobalErrorCode.NOT_FOUND));
    }

    private String generateUniqueReferenceNumber() {
        return "TRX" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
    }

//    private void sendKafkaNotification(TransactionNotificationDto notificationDto) {
//        try {
//            kafkaTemplate.send(NOTIFICATION_TOPIC, notificationDto.getReferenceNumber(), notificationDto);
//            log.info("Kafka notification sent for transaction: {}", notificationDto.getReferenceNumber());
//        } catch (Exception e) {
//            log.error("Failed to send Kafka notification for transaction: {}", notificationDto.getReferenceNumber(), e);
//            // Optionally, you might want to implement a retry mechanism or compensating action here
//        }
//    }

    @Transactional
    @Override
    public Response internalFundTransfer(TransactionDto request) {
        String referenceNumber = generateUniqueReferenceNumber();

        Account accountSource = getAndValidateAccount(request.getFromAccountNumber());
        Account accountDestination = getAndValidateAccount(request.getToAccountNumber());

        validateBalance(accountSource, request.getAmount());

        try {
            TransactionDto transactionDto = TransactionDto.builder()
                    .referenceNumber(referenceNumber)
                    .fromBank("Kien Long Bank")
                    .fromAccountHolderName(request.getFromAccountHolderName())
                    .fromAccountNumber(request.getFromAccountNumber())
                    .toBank("Kien Long Bank")
                    .toAccountHolderName(request.getToAccountHolderName())
                    .toAccountNumber(request.getToAccountNumber())
                    .transactionType(INTERNAL_TRANSFER.name())
                    .status(COMPLETED.name())
                    .transactionDate(LocalDateTime.now())
                    .amount(request.getAmount().negate())
                    .build();

            Transaction transaction = mapper.convertToEntity(transactionDto);

            accountSource.setAvailableBalance(accountSource.getAvailableBalance().subtract(request.getAmount()));
            accountDestination.setAvailableBalance(accountDestination.getAvailableBalance().add(request.getAmount()));

            repository.save(transaction);
            log.info("Internal fund transfer completed successfully");

            TransactionNotificationDto notificationDto = TransactionNotificationDto.builder()
                    .referenceNumber(referenceNumber)
                    .transactionType(TransactionType.INTERNAL_TRANSFER.name())
                    .fromAccountHolderName(request.getFromAccountHolderName())
                    .fromAccountNumber(request.getFromAccountNumber())
                    .fromBank("Kien Long Bank")
                    .toAccountHolderName(request.getToAccountHolderName())
                    .toAccountNumber(request.getToAccountNumber())
                    .toBank("Kien Long Bank")
                    .amount(request.getAmount())
                    .transactionDate(LocalDateTime.now())
                    .build();
//            kafkaTemplate.send("transaction-notifications", notificationDto);
        } catch (Exception e) {
            log.error("Error during internal fund transfer: {}", e.getMessage(), e);
            throw new RuntimeException("Internal server error, please try again later");
        }
        return Response.builder()
                .message("Transaction successfully completed")
                .build();
    }
}
