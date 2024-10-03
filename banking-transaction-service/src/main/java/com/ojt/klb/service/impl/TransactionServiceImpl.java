package com.ojt.klb.service.impl;

import com.ojt.klb.exception.*;
import com.ojt.klb.external.AccountClient;
import com.ojt.klb.kafka.TransactionNotification;
import com.ojt.klb.kafka.TransactionProducer;
import com.ojt.klb.model.TransactionStatus;
import com.ojt.klb.model.TransactionType;
import com.ojt.klb.model.dto.TransactionDto;
import com.ojt.klb.model.entity.Transaction;
import com.ojt.klb.model.external.Account;
import com.ojt.klb.model.mapper.TransactionMapper;
import com.ojt.klb.model.request.TransactionRequest;
import com.ojt.klb.model.response.ApiResponse;
import com.ojt.klb.repository.TransactionRepository;
import com.ojt.klb.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository repository;
    private final AccountClient accountClient;
    private final TransactionMapper mapper = new TransactionMapper();
    private final TransactionProducer transactionProducer;

    @Override
    public ApiResponse handleTransaction(TransactionDto transactionDto) {
        Account account;
        ResponseEntity<ApiResponse<Account>> response = accountClient.getDataAccountNumber(transactionDto.getAccountNumber());
        ApiResponse<Account> apiResponse = response.getBody();
        if (Objects.isNull(apiResponse) || !apiResponse.isSuccess()) {
            log.error("requested account " + transactionDto.getAccountNumber() + " is not found on the server");
            throw new ResourceNotFound("Requested account not found on the server", GlobalErrorCode.NOT_FOUND);
        }
        account = apiResponse.getData();
        System.out.println(account);
        Transaction transaction = mapper.convertToEntity(transactionDto);
        if(transactionDto.getTransactionType().equals(TransactionType.DEPOSIT.toString())) {
            if (transactionDto.getAmount().compareTo(BigDecimal.valueOf(50000)) < 0) {
                throw new TransactionException("The minimum deposit amount is 50,000 VND.");
            }
            account.setBalance(account.getBalance().add(transactionDto.getAmount()));
        } else if (transactionDto.getTransactionType().equals(TransactionType.WITHDRAWAL.toString())) {
            if(!account.getStatus().equals(Account.Status.active)){
                log.error("account is either inactive/closed, cannot process the transaction");
                throw new AccountStatusException("account is inactive or closed");
            }
            if (transactionDto.getAmount().compareTo(BigDecimal.valueOf(50000)) < 0) {
                throw new TransactionException("The minimum withdraw amount is 50,000 VND.");
            }
            if(account.getBalance().compareTo(transactionDto.getAmount()) < 0){
                log.error("insufficient balance in the account");
                throw new InsufficientBalance("Insufficient balance in the account");
            }
            transaction.setAmount(transactionDto.getAmount().negate());
            account.setBalance(account.getBalance().subtract(transactionDto.getAmount()));
        }

        String referenceNumber = generateUniqueReferenceNumber();
        transaction.setTransactionType(TransactionType.valueOf(transactionDto.getTransactionType()));
        transaction.setDescription(transactionDto.getDescription());
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setReferenceNumber(referenceNumber);

        accountClient.updateAccount(transactionDto.getAccountNumber(), account);

        repository.save(transaction);

        BigDecimal balance = accountClient.accountBalance(account.getAccountNumber()).getBody();

        transactionProducer.sendTransactionNotification(
                new TransactionNotification(
                        referenceNumber,
                        account.getCustomerId(),
                        account,
                        account.getAccountName(),
                        balance,
                        transactionDto.getTransactionType(),
                        transactionDto.getAmount(),
                        LocalDateTime.now(),
                        transactionDto.getDescription()
                )
        );

        return ApiResponse.builder()
                .message("Transaction completed successfully")
                .status(200)
                .success(true)
                .build();
    }


    private String generateUniqueReferenceNumber() {
        return "TRX" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
    }

    @Override
    public List<TransactionRequest> getTransaction(String accountNumber) {
        return repository.findByAccountNumber(accountNumber)
                .stream().map(transaction -> {
                    TransactionRequest transactionRequest = new TransactionRequest();
                    BeanUtils.copyProperties(transaction, transactionRequest);
                    transactionRequest.setTransactionStatus(transaction.getStatus().toString());
                    transactionRequest.setLocalDateTime(transaction.getTransactionDate());
                    transactionRequest.setTransactionType(transaction.getTransactionType().toString());
                    return transactionRequest;
                }).collect(Collectors.toList());
    }

    @Override
    public List<TransactionRequest> getTransactionByTransactionReference(String transactionReference) {
        return repository.findByReferenceNumber(transactionReference)
                .stream().map(transaction -> {
                    TransactionRequest transactionRequest = new TransactionRequest();
                    BeanUtils.copyProperties(transaction, transactionRequest);
                    transactionRequest.setTransactionStatus(transaction.getStatus().toString());
                    transactionRequest.setLocalDateTime(transaction.getTransactionDate());
                    transactionRequest.setTransactionType(transaction.getTransactionType().toString());
                    return transactionRequest;
                }).collect(Collectors.toList());
    }

    @Override
    public List<TransactionDto> findTransactions(TransactionType transactionType, LocalDateTime fromDate, LocalDateTime toDate, TransactionStatus status) {
        List<Transaction> transactions = repository.findByTransactionTypeAndTransactionDateBetweenAndStatus(
                transactionType, fromDate, toDate, status
        );

        return transactions.stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ApiResponse saveTransaction(List<TransactionDto> transactionDtos, String transactionReference) {
        List<Transaction> transactions = mapper.convertToEntityList(transactionDtos);

        transactions.forEach(transaction -> {
            transaction.setTransactionType(TransactionType.INTERNAL_TRANSFER);
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction.setReferenceNumber(transactionReference);
        });

        repository.saveAll(transactions);
        return ApiResponse.builder()
                .status(200)
                .success(true)
                .message("Transaction completed successfully").build();
    }
}
