package com.ojt.klb.service.impl;

import com.ojt.klb.exception.*;
import com.ojt.klb.external.AccountClient;
import com.ojt.klb.model.TransactionStatus;
import com.ojt.klb.model.TransactionType;
import com.ojt.klb.model.dto.TransactionDto;
import com.ojt.klb.model.entity.Transaction;
import com.ojt.klb.model.external.Account;
import com.ojt.klb.model.external.AccountStatus;
import com.ojt.klb.model.mapper.TransactionMapper;
import com.ojt.klb.model.request.TransactionRequest;
import com.ojt.klb.model.response.Response;
import com.ojt.klb.repository.TransactionRepository;
import com.ojt.klb.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    @Override
    public Response handleTransaction(TransactionDto transactionDto) {
        ResponseEntity<Account> response = accountClient.readByAccountNumber(transactionDto.getAccountNumber());
        if (Objects.isNull(response.getBody())){
            throw new ResourceNotFound("Requested account not found on the server", GlobalErrorCode.NOT_FOUND);
        }
        Account account = response.getBody();
        Transaction transaction = mapper.convertToEntity(transactionDto);
        if(transactionDto.getTransactionType().equals(TransactionType.DEPOSIT.toString())) {
            account.setAvailableBalance(account.getAvailableBalance().add(transactionDto.getAmount()));
        } else if (transactionDto.getTransactionType().equals(TransactionType.WITHDRAWAL.toString())) {
            if(!account.getAccountStatus().equals("ACTIVE")){
                log.error("account is either inactive/closed, cannot process the transaction");
                throw new AccountStatusException("account is inactive or closed");
            }
            if(account.getAvailableBalance().compareTo(transactionDto.getAmount()) < 0){
                log.error("insufficient balance in the account");
                throw new InsufficientBalance("Insufficient balance in the account");
            }
            transaction.setAmount(transactionDto.getAmount().negate());
            account.setAvailableBalance(account.getAvailableBalance().subtract(transactionDto.getAmount()));
        }

        transaction.setTransactionType(TransactionType.valueOf(transactionDto.getTransactionType()));
        transaction.setDescription(transactionDto.getDescription());
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setReferenceNumber(generateUniqueReferenceNumber());

        accountClient.updateAccount(transactionDto.getAccountNumber(), account);
        repository.save(transaction);
        return Response.builder()
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

//    private void sendKafkaNotification(TransactionNotificationDto notificationDto) {
//        try {
//            kafkaTemplate.send(NOTIFICATION_TOPIC, notificationDto.getReferenceNumber(), notificationDto);
//            log.info("Kafka notification sent for transaction: {}", notificationDto.getReferenceNumber());
//        } catch (Exception e) {
//            log.error("Failed to send Kafka notification for transaction: {}", notificationDto.getReferenceNumber(), e);
//            // Optionally, you might want to implement a retry mechanism or compensating action here
//        }
//    }

    @Override
    public Response internalTransaction(List<TransactionDto> transactionDtos, String referenceNumber) {
        List<Transaction> transactions = mapper.convertToEntityList(transactionDtos);

        transactions.forEach(transaction -> {
            transaction.setTransactionType(TransactionType.INTERNAL_TRANSFER);
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction.setReferenceNumber(referenceNumber);
        });

        repository.saveAll(transactions);
        return Response.builder()
                .status(200)
                .success(true)
                .message("Transaction completed successfully").build();
    }


}
