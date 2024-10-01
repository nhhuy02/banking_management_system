package com.ojt.klb.service.impl;

import com.ojt.klb.exception.*;
import com.ojt.klb.external.AccountClient;
import com.ojt.klb.external.TransactionClient;
import com.ojt.klb.kafka.InternalTransferNotification;
import com.ojt.klb.kafka.InternalTransferProducer;
import com.ojt.klb.model.TransactionStatus;
import com.ojt.klb.model.TransferType;
import com.ojt.klb.model.dto.Account;
import com.ojt.klb.model.dto.FundTransferDto;
import com.ojt.klb.model.dto.Transaction;
import com.ojt.klb.model.entity.FundTransfer;
import com.ojt.klb.model.mapper.FundTransferMapper;
import com.ojt.klb.model.request.FundTransferRequest;
import com.ojt.klb.model.response.ApiResponse;
import com.ojt.klb.model.response.FundTransferResponse;
import com.ojt.klb.repository.FundTransferRepository;
import com.ojt.klb.service.FundTransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FundTransferServiceImpl implements FundTransferService {
    private final AccountClient accountClient;
    private final TransactionClient transactionClient;
    private final FundTransferRepository fundTransferRepository;
    private final InternalTransferProducer internalTransferProducer;

    private final FundTransferMapper fundTransferMapper = new FundTransferMapper();

    @Override
    public FundTransferResponse fundTransfer(FundTransferRequest fundTransferRequest) {
        Account fromAccount;
        ResponseEntity<ApiResponse<Account>> response = accountClient.getDataAccountNumber(fundTransferRequest.getFromAccount());
        ApiResponse<Account> apiResponse = response.getBody();
        if (Objects.isNull(apiResponse) || !apiResponse.isSuccess()) {
            log.error("requested account " + fundTransferRequest.getFromAccount() + " is not found on the server");
            throw new AccountNotFoundException("Requested account not found on the server", GlobalErrorCode.NOT_FOUND);
        }
        fromAccount = apiResponse.getData();

        if (!fromAccount.getStatus().equals(Account.Status.active)) {
            log.error("Account status is pending or inactive, please update the account status");
            throw new AccountUpdateException("Account status is pending", GlobalErrorCode.NOT_ACCEPTABLE);
        }
        BigDecimal minimumAmount = new BigDecimal("2000");
        if (fundTransferRequest.getAmount().compareTo(minimumAmount) < 0) {
            log.error("Transfer amount is below the minimum requirement of 2000");
            throw new InvalidTransferAmountException("Transfer amount must be at least 2000", GlobalErrorCode.INVALID_AMOUNT);
        }

        if (fromAccount.getBalance().compareTo(fundTransferRequest.getAmount()) < 0) {
            log.error("Required amount to transfer is not available");
            throw new InsufficientBalance("requested amount is not available", GlobalErrorCode.NOT_ACCEPTABLE);
        }

        BigDecimal dailyLimit = new BigDecimal("500000000");
        BigDecimal dailyTotal = getDailyTransferTotal(fromAccount.getAccountNumber());
        BigDecimal newTotal = dailyTotal.add(fundTransferRequest.getAmount());

        if (newTotal.compareTo(dailyLimit) > 0) {
            log.error("Daily transfer limit exceeded");
            throw new DailyLimitExceededException("Daily transfer limit of 500000000 exceeded", GlobalErrorCode.NOT_ACCEPTABLE);
        }

        Account toAccount;
        response = accountClient.getDataAccountNumber(fundTransferRequest.getToAccount());
        ApiResponse<Account> toAccountResponse = response.getBody();

        if (Objects.isNull(toAccountResponse) || !toAccountResponse.isSuccess() || Objects.isNull(toAccountResponse.getData())) {
            log.error("Requested account " + fundTransferRequest.getToAccount() + " is not found on the server");
            throw new AccountNotFoundException("Requested account not found on the server", GlobalErrorCode.NOT_FOUND);
        }

        toAccount = toAccountResponse.getData();

        String transactionReference = internalTransfer(fromAccount, toAccount, fundTransferRequest.getAmount(), fundTransferRequest.getDescription());
        LocalDateTime transferredOn = LocalDateTime.now();
        FundTransfer fundTransfer = FundTransfer.builder()
                .transferType(TransferType.INTERNAL)
                .amount(fundTransferRequest.getAmount())
                .fromAccount(fromAccount.getAccountNumber())
                .transactionReference(transactionReference)
                .status(TransactionStatus.SUCCESS)
                .toAccount(toAccount.getAccountNumber())
                .transferredOn(transferredOn)
                .description(fundTransferRequest.getDescription())
                .build();

        fundTransferRepository.save(fundTransfer);


        var fromAccountBalance = accountClient.accountBalance(fromAccount.getAccountNumber());
        var toAccountBalance = accountClient.accountBalance(toAccount.getAccountNumber());

//        internalTransferProducer.sendInternalTransferNotification(
//                new InternalTransferNotification(
//                        fromAccount.getEmail(),
//                        toAccount.getEmail(),
//                        transactionReference,
//                        TransferType.INTERNAL,
//                        transferredOn,
//                        fromAccount.getAccountNumber(),
//                        toAccount.getAccountNumber(),
//                        toAccount.getAccountName(),
//                        fundTransferRequest.getAmount(),
//                        fundTransfer.getDescription(),
//                        fromAccountBalance,
//                        toAccountBalance
//                )
//        );
        return FundTransferResponse.builder()
                .transactionReference(transactionReference)
                .message("Fund transfer was successful").build();
    }

    private BigDecimal getDailyTransferTotal(String accountNumber) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        List<FundTransfer> dailyTransfers = fundTransferRepository.findByFromAccountAndTransferredOnBetween(
                accountNumber, startOfDay, endOfDay);

        return dailyTransfers.stream()
                .map(FundTransfer::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String internalTransfer(Account fromAccount, Account toAccount, BigDecimal amount, String description) {
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        accountClient.updateAccount(fromAccount.getAccountNumber(), fromAccount);

        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountClient.updateAccount(toAccount.getAccountNumber(), toAccount);

        List<Transaction> transactions = List.of(
                Transaction.builder()
                        .accountNumber(fromAccount.getAccountNumber())
                        .transactionType("INTERNAL_TRANSFER")
                        .amount(amount)
                        .description(description)
                        .build(),
                Transaction.builder()
                        .accountNumber(toAccount.getAccountNumber())
                        .transactionType("INTERNAL_TRANSFER")
                        .amount(amount)
                        .description(description)
                        .build());

        String transactionReference = generateUniqueTransactionReference();
        transactionClient.makeInternalTransactions(transactions, transactionReference);
        return transactionReference;
    }

    private String generateUniqueTransactionReference() {
        return "INT" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
    }


    @Override
    public FundTransferDto getTransferDetailsFromReferenceNumber(String referenceNumber) {
        return fundTransferRepository.findByTransactionReference(referenceNumber)
                .map(fundTransferMapper::convertToDto)
                .orElseThrow(() -> new ResourceNotFound("Fund transfer not found", GlobalErrorCode.NOT_FOUND));
    }

    @Override
    public List<FundTransferDto> getAllTransferByAccountNumber(String accountNumber) {
        List<FundTransfer> transfers = fundTransferRepository.findByFromAccountOrToAccount(accountNumber);
        return transfers.stream()
                .map(transfer -> {
                    FundTransferDto dto = fundTransferMapper.convertToDto(transfer);
                    if (transfer.getFromAccount().equals(accountNumber)) {
                        dto.setTransferType(TransferType.valueOf("OUTGOING"));
                    } else {
                        dto.setTransferType(TransferType.valueOf("INCOMING"));
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
