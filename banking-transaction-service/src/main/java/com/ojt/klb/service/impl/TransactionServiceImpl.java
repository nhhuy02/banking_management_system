package com.ojt.klb.service.impl;

import com.ojt.klb.exception.*;
import com.ojt.klb.external.AccountClient;
import com.ojt.klb.kafka.TransactionNotification;
import com.ojt.klb.kafka.TransactionProducer;
import com.ojt.klb.model.TransactionStatus;
import com.ojt.klb.model.TransactionType;
import com.ojt.klb.model.dto.SearchDataDto;
import com.ojt.klb.model.dto.TransactionDto;
import com.ojt.klb.model.entity.Transaction;
import com.ojt.klb.model.external.Account;
import com.ojt.klb.model.mapper.TransactionMapper;
import com.ojt.klb.model.response.ApiResponse;
import com.ojt.klb.model.response.TransactionResponse;
import com.ojt.klb.repository.TransactionRepository;
import com.ojt.klb.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        ApiResponse<Account> apiResponse = accountClient.getDataAccountNumber(transactionDto.getAccountNumber()).getBody();
        if (Objects.isNull(apiResponse) || !apiResponse.isSuccess()) {
            log.error("requested account " + transactionDto.getAccountNumber() + " is not found on the server");
            throw new ResourceNotFound("Requested account not found on the server", GlobalErrorCode.NOT_FOUND);
        }
        account = apiResponse.getData();

        Transaction transaction = mapper.convertToEntity(transactionDto);

        BigDecimal accountBalanceBeforeTransaction = account.getBalance();

        if (transactionDto.getTransactionType().equals(TransactionType.DEPOSIT.toString())) {
            if (transactionDto.getAmount().compareTo(BigDecimal.valueOf(50000)) < 0) {
                throw new TransactionException("The minimum deposit amount is 50,000 VND.");
            }

            account.setBalance(account.getBalance().add(transactionDto.getAmount()));

        } else if (transactionDto.getTransactionType().equals(TransactionType.WITHDRAWAL.toString())) {
            if (!account.getStatus().equals(Account.Status.active)) {
                log.error("account is either inactive/closed, cannot process the transaction");
                throw new AccountStatusException("account is inactive or closed");
            }
            if (transactionDto.getAmount().compareTo(BigDecimal.valueOf(50000)) < 0) {
                throw new TransactionException("The minimum withdraw amount is 50,000 VND.");
            }
            if (account.getBalance().compareTo(transactionDto.getAmount()) < 0) {
                log.error("insufficient balance in the account");
                throw new InsufficientBalance("Insufficient balance in the account");
            }

            account.setBalance(account.getBalance().subtract(transactionDto.getAmount()));
            transaction.setAmount(transactionDto.getAmount().negate());
        }

        BigDecimal accountBalanceAfterTransaction = account.getBalance();

        String referenceNumber = generateUniqueReferenceNumber();
        transaction.setTransactionType(TransactionType.valueOf(transactionDto.getTransactionType()));
        transaction.setDescription(transactionDto.getDescription());
        transaction.setBalanceBeforeTransaction(accountBalanceBeforeTransaction);
        transaction.setBalanceAfterTransaction(accountBalanceAfterTransaction);
        transaction.setFee(BigDecimal.valueOf(0));
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setReferenceNumber(referenceNumber);

        accountClient.updateAccount(transactionDto.getAccountNumber(), account);
        repository.save(transaction);

        BigDecimal balance = accountClient.accountBalance(account.getAccountNumber()).getBody();

        transactionProducer.sendTransactionNotification(
                new TransactionNotification(
                        apiResponse.getData().getEmail(),
                        referenceNumber,
                        account.getCustomerId(),
                        apiResponse.getData().getAccountNumber(),
                        apiResponse.getData().getFullName(),
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
        return "TRN" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
    }

    @Override
    public List<TransactionResponse> getTransaction(String accountNumber) {
        return repository.findByAccountNumber(accountNumber)
                .stream().map(transaction -> {
                    TransactionResponse transactionResponse = new TransactionResponse();
                    BeanUtils.copyProperties(transaction, transactionResponse);
                    transactionResponse.setTransactionStatus(transaction.getStatus().toString());
                    transactionResponse.setLocalDateTime(transaction.getTransactionDate());
                    transactionResponse.setTransactionType(transaction.getTransactionType().toString());
                    return transactionResponse;
                }).collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponse> getTransactionByTransactionReference(String transactionReference) {
        return repository.findByReferenceNumber(transactionReference)
                .stream().map(transaction -> {
                    TransactionResponse transactionResponse = new TransactionResponse();
                    BeanUtils.copyProperties(transaction, transactionResponse);
                    transactionResponse.setTransactionStatus(transaction.getStatus().toString());
                    transactionResponse.setLocalDateTime(transaction.getTransactionDate());
                    transactionResponse.setTransactionType(transaction.getTransactionType().toString());
                    return transactionResponse;
                }).collect(Collectors.toList());
    }


    @Override
    public List<SearchDataDto> findTransactions(
            String accountNumber,
            TransactionType transactionType,
            LocalDate fromDate,
            LocalDate toDate,
            TransactionStatus status) {

        Specification<Transaction> spec = (root, query, cb) -> {
            query.distinct(true);
            return null;
        };

        if (accountNumber != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("accountNumber"), accountNumber));
        }

        if (transactionType != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("transactionType"), transactionType));
        }

        if (fromDate != null) {
            LocalDateTime fromDateTime = fromDate.atStartOfDay();
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("transactionDate"), fromDateTime));
        }

        if (toDate != null) {
            LocalDateTime toDateTime = toDate.plusDays(1).atStartOfDay().minusNanos(1);
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("transactionDate"), toDateTime));
        }

        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        List<Transaction> transactions = repository.findAll(spec);

        return transactions.stream()
                .map(transaction -> {
                    SearchDataDto dto = new SearchDataDto();
                    dto.setId(transaction.getId());
                    dto.setAccountNumber(transaction.getAccountNumber());
                    dto.setReferenceNumber(transaction.getReferenceNumber());
                    dto.setTransactionType(transaction.getTransactionType().name());
                    dto.setAmount(transaction.getAmount());
                    dto.setDescription(transaction.getDescription());
                    dto.setTransactionDate(transaction.getTransactionDate());
                    dto.setBalanceBeforeTransaction(transaction.getBalanceBeforeTransaction());
                    dto.setBalanceAfterTransaction(transaction.getBalanceAfterTransaction());
                    dto.setFee(transaction.getFee());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ApiResponse saveUtilityPaymentTransaction(TransactionDto transactionDto, String referenceNumber) {
        Transaction transaction = mapper.convertToEntity(transactionDto);

        transaction.setTransactionType(TransactionType.UTILITY_PAYMENT);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setReferenceNumber(referenceNumber);

        repository.save(transaction);
        return ApiResponse.builder()
                .status(200)
                .success(true)
                .message("Transaction completed successfully").build();
    }

    @Override
    public SearchDataDto findLastTransactionByAccountNumberBeforeDate(String accountNumber, LocalDate dateBefore) {
        return null;
    }


    @Override
    public ApiResponse saveInternalTransaction(List<TransactionDto> transactionDtos, String transactionReference) {
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

    @Override
    public ApiResponse saveExternalTransaction(List<TransactionDto> transactionDtos, String transactionReference) {
        List<Transaction> transactions = mapper.convertToEntityList(transactionDtos);

        transactions.forEach(transaction -> {
            transaction.setTransactionType(TransactionType.EXTERNAL_TRANSFER);
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
