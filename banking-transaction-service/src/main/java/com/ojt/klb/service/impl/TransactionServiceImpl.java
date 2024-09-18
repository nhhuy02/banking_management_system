package com.ojt.klb.service.impl;

import com.ojt.klb.exception.GlobalErrorCode;
import com.ojt.klb.exception.InsufficientBalance;
import com.ojt.klb.exception.ResourceNotFound;
import com.ojt.klb.exception.TransactionFailedException;
import com.ojt.klb.external.AccountService;
import com.ojt.klb.model.TransactionStatus;
import com.ojt.klb.model.TransactionType;
import com.ojt.klb.model.dto.TransactionDto;
import com.ojt.klb.model.entity.Transaction;
import com.ojt.klb.model.external.Account;
import com.ojt.klb.model.mapper.TransactionMapper;
import com.ojt.klb.model.request.InternalTransferRequest;
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
import static com.ojt.klb.model.TransactionStatus.PENDING;
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
                .transactionType(String.valueOf(TransactionType.DEPOSIT))
                .amount(amount)
                .transactionDate(LocalDateTime.now())
                .status(String.valueOf(PENDING))
                .build();

        Transaction transaction = mapper.convertToEntity(internalTransferDto);
        repository.save(transaction);

        try {
            account.setAvailableBalance(account.getAvailableBalance().add(amount));
            transaction.setStatus(COMPLETED);
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
                .transactionType(String.valueOf(TransactionType.WITHDRAWAL))
                .amount(amount)
                .transactionDate(LocalDateTime.now())
                .status(String.valueOf(PENDING))
                .build();

        Transaction transaction = mapper.convertToEntity(internalTransferDto);
        repository.save(transaction);

        try {
            account.setAvailableBalance(account.getAvailableBalance().subtract(amount));
            transaction.setStatus(COMPLETED);
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

    private void validateBalance(Account account, BigDecimal amount){
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

    @Transactional
    @Override
    public Response internalFundTransfer(InternalTransferRequest request){
        String referenceNumber = generateUniqueReferenceNumber();

        Account accountSource = getAndValidateAccount(request.getFromAccountNumber());
        Account accountDestination = getAndValidateAccount(request.getToAccountNumber());

        validateBalance(accountSource, request.getAmount());

        try{
            TransactionDto sourceInternalTransferDto = TransactionDto.builder()
                    .referenceNumber(referenceNumber)
                    .transactionType(String.valueOf(TransactionType.WITHDRAWAL))
                    .status(String.valueOf(PENDING))
                    .accountId(accountSource.getAccountId())
                    .transactionDate(LocalDateTime.now())
                    .amount(request.getAmount().negate())
                    .build();

            TransactionDto destinationTransactionDto = TransactionDto.builder()
                    .referenceNumber(referenceNumber)
                    .transactionType(String.valueOf(INTERNAL_TRANSFER))
                    .status(String.valueOf(PENDING))
                    .accountId(accountDestination.getAccountId())
                    .transactionDate(LocalDateTime.now())
                    .amount(request.getAmount())
                    .build();

            Transaction sourceTransaction = mapper.convertToEntity(sourceInternalTransferDto);
            Transaction destinationTransaction = mapper.convertToEntity(destinationTransactionDto);

            accountSource.setAvailableBalance(accountSource.getAvailableBalance().subtract(request.getAmount()));
            accountDestination.setAvailableBalance(accountDestination.getAvailableBalance().add(request.getAmount()));

            accountSource.setAccountStatus(String.valueOf(COMPLETED));
            accountDestination.setAccountStatus(String.valueOf(COMPLETED));

            repository.save(sourceTransaction);
            repository.save(destinationTransaction);

            log.info("Internal fund transfer completed successfully");
        }catch (Exception e) {
            log.error("Error during internal fund transfer: {}", e.getMessage(), e);
            throw new RuntimeException("Internal server error, please try again later");
        }
        return Response.builder()
                .message("Transaction successfully completed")
                .build();
    }
}
