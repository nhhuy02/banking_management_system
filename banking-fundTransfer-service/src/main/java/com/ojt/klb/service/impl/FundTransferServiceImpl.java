package com.ojt.klb.service.impl;

import com.ojt.klb.exception.*;
import com.ojt.klb.external.AccountClient;
import com.ojt.klb.external.SimulatorApiClient;
import com.ojt.klb.external.TransactionClient;
import com.ojt.klb.kafka.InternalTransferNotification;
import com.ojt.klb.kafka.InternalTransferProducer;
import com.ojt.klb.model.TransactionStatus;
import com.ojt.klb.model.TransferType;
import com.ojt.klb.model.dto.Account;
import com.ojt.klb.model.dto.BankAccount;
import com.ojt.klb.model.dto.FundTransferDto;
import com.ojt.klb.model.dto.Transaction;
import com.ojt.klb.model.entity.FundTransfer;
import com.ojt.klb.model.mapper.FundTransferMapper;
import com.ojt.klb.model.request.FundTransferRequest;
import com.ojt.klb.model.request.InterFundTransferRequest;
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
    private final SimulatorApiClient simulatorApiClient;

    private final FundTransferMapper fundTransferMapper = new FundTransferMapper();

    @Override
    public FundTransferResponse internalTransfer(FundTransferRequest fundTransferRequest) {
        Account fromAccount = validateFromAccount(fundTransferRequest.getFromAccount(), fundTransferRequest.getAmount());

        Account toAccount;
        var response = accountClient.getDataAccountNumber(fundTransferRequest.getToAccount());
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

        var fromAccountBalance  = accountClient.accountBalance(fromAccount.getAccountNumber()).getBody();
        var toAccountBalance = accountClient.accountBalance(toAccount.getAccountNumber()).getBody();

        internalTransferProducer.sendInternalTransferNotification(
                new InternalTransferNotification(
                        fromAccount.getEmail(),
                        toAccount.getEmail(),
                        fromAccount.getCustomerId(),
                        toAccount.getCustomerId(),
                        transactionReference,
                        TransferType.INTERNAL.toString(),
                        transferredOn,
                        fromAccount.getAccountNumber(),
                        toAccount.getAccountNumber(),
                        toAccountResponse.getData().getFullName(),
                        fundTransferRequest.getAmount(),
                        fundTransfer.getDescription(),
                        fromAccountBalance,
                        toAccountBalance
                )
        );
        return FundTransferResponse.builder()
                .transactionReference(transactionReference)
                .message("Fund transfer was successful").build();
    }

    @Override
    public FundTransferResponse interTransfer(InterFundTransferRequest interFundTransferRequest) {
        Account fromAccount = validateFromAccount(interFundTransferRequest.getFromAccount(), interFundTransferRequest.getAmount());

        ResponseEntity<Boolean> validationResponse = simulatorApiClient.validateAccount(
                interFundTransferRequest.getToAccount(),
                interFundTransferRequest.getBankName()
        );

        if (validationResponse == null || validationResponse.getBody() == null || !validationResponse.getBody()) {
            log.error("Account validation failed or returned null for account: {} and bank: {}",
                    interFundTransferRequest.getToAccount(), interFundTransferRequest.getBankName());
            throw new AccountNotFoundException("Account validation failed", GlobalErrorCode.NOT_FOUND);
        }

        ResponseEntity<BankAccount> toAccountResponse = simulatorApiClient.readAccountByAccountNumber(interFundTransferRequest.getToAccount());
        if (toAccountResponse == null || toAccountResponse.getBody() == null) {
            log.error("Failed to retrieve account details for account: {}", interFundTransferRequest.getToAccount());
            throw new AccountNotFoundException("Failed to retrieve account details", GlobalErrorCode.NOT_FOUND);
        }
        BankAccount toAccount = toAccountResponse.getBody();

        String transactionReference = interTransfer(fromAccount, toAccount, interFundTransferRequest.getAmount(), interFundTransferRequest.getDescription());
        LocalDateTime transferredOn = LocalDateTime.now();

        FundTransfer fundTransfer = FundTransfer.builder()
                .transferType(TransferType.EXTERNAL)
                .amount(interFundTransferRequest.getAmount())
                .fromAccount(fromAccount.getAccountNumber())
                .transactionReference(transactionReference)
                .status(TransactionStatus.SUCCESS)
                .toAccount(toAccount.getAccountNumber())
                .transferredOn(transferredOn)
                .description(interFundTransferRequest.getDescription())
                .build();

        fundTransferRepository.save(fundTransfer);

        var fromAccountBalance  = accountClient.accountBalance(fromAccount.getAccountNumber()).getBody();
        var toAccountBalance = simulatorApiClient.accountBalance(toAccount.getAccountNumber()).getBody();

//        internalTransferProducer.sendInternalTransferNotification(
//                new InternalTransferNotification(
//                        fromAccount.getEmail(),
//                        toAccount.getEmail(),
//                        fromAccount.getCustomerId(),
//                        toAccount.getCustomerId(),
//                        transactionReference,
//                        TransferType.INTERNAL.toString(),
//                        transferredOn,
//                        fromAccount.getAccountNumber(),
//                        toAccount.getAccountNumber(),
//                        toAccountResponse.getData().getFullName(),
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
        transactionClient.saveTransaction(transactions, transactionReference);
        return transactionReference;
    }

    private String interTransfer(Account fromAccount, BankAccount toAccount, BigDecimal amount, String description){
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        accountClient.updateAccount(fromAccount.getAccountNumber(), fromAccount);

        toAccount.setBalance(toAccount.getBalance().add(amount));
        simulatorApiClient.updateAccount(toAccount.getAccountNumber(), toAccount);

        List<Transaction> transactions = List.of(
                Transaction.builder()
                        .accountNumber(fromAccount.getAccountNumber())
                        .transactionType("EXTERNAL_TRANSFER")
                        .amount(amount)
                        .description(description)
                        .build(),
                Transaction.builder()
                        .accountNumber(toAccount.getAccountNumber())
                        .transactionType("EXTERNAL_TRANSFER")
                        .amount(amount)
                        .description(description)
                        .build());

        String transactionReference = generateUniqueTransactionReference();
        transactionClient.saveTransaction(transactions, transactionReference);
        return transactionReference;
    }

    private Account validateFromAccount(String fromAccountNumber, BigDecimal transferAmount) {
        ResponseEntity<ApiResponse<Account>> response = accountClient.getDataAccountNumber(fromAccountNumber);
        ApiResponse<Account> apiResponse = response.getBody();

        if (Objects.isNull(apiResponse) || !apiResponse.isSuccess()) {
            log.error("Requested account " + fromAccountNumber + " is not found on the server");
            throw new AccountNotFoundException("Requested account not found on the server", GlobalErrorCode.NOT_FOUND);
        }

        Account fromAccount = apiResponse.getData();

        if (!fromAccount.getStatus().equals(Account.Status.active)) {
            log.error("Account status is pending or inactive, please update the account status");
            throw new AccountUpdateException("Account status is pending", GlobalErrorCode.NOT_ACCEPTABLE);
        }

        BigDecimal minimumAmount = new BigDecimal("2000");
        if (transferAmount.compareTo(minimumAmount) < 0) {
            log.error("Transfer amount is below the minimum requirement of 2000");
            throw new InvalidTransferAmountException("Transfer amount must be at least 2000", GlobalErrorCode.INVALID_AMOUNT);
        }

        if (fromAccount.getBalance().compareTo(transferAmount) < 0) {
            log.error("Required amount to transfer is not available");
            throw new InsufficientBalance("Requested amount is not available", GlobalErrorCode.NOT_ACCEPTABLE);
        }

        BigDecimal dailyLimit = new BigDecimal("500000000");
        BigDecimal dailyTotal = getDailyTransferTotal(fromAccount.getAccountNumber());
        BigDecimal newTotal = dailyTotal.add(transferAmount);

        if (newTotal.compareTo(dailyLimit) > 0) {
            log.error("Daily transfer limit exceeded");
            throw new DailyLimitExceededException("Daily transfer limit of 500000000 exceeded", GlobalErrorCode.NOT_ACCEPTABLE);
        }
        return fromAccount;
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
