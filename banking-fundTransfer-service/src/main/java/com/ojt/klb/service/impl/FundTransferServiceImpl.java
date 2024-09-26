package com.ojt.klb.service.impl;

import com.ojt.klb.exception.AccountUpdateException;
import com.ojt.klb.exception.GlobalErrorCode;
import com.ojt.klb.exception.InsufficientBalance;
import com.ojt.klb.exception.ResourceNotFound;
import com.ojt.klb.external.AccountClient;
import com.ojt.klb.external.TransactionClient;
import com.ojt.klb.model.TransactionStatus;
import com.ojt.klb.model.TransferType;
import com.ojt.klb.model.dto.Account;
import com.ojt.klb.model.dto.FundTransferDto;
import com.ojt.klb.model.dto.Transaction;
import com.ojt.klb.model.entity.FundTransfer;
import com.ojt.klb.model.mapper.FundTransferMapper;
import com.ojt.klb.model.request.FundTransferRequest;
import com.ojt.klb.model.response.FundTransferResponse;
import com.ojt.klb.repository.FundTransferRepository;
import com.ojt.klb.service.FundTransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FundTransferServiceImpl implements FundTransferService {
    private final AccountClient accountClient;
    private final TransactionClient transactionClient;
    private final FundTransferRepository fundTransferRepository;

    private final FundTransferMapper fundTransferMapper = new FundTransferMapper();

    @Override
    public FundTransferResponse fundTransfer(FundTransferRequest fundTransferRequest) {
        Account fromAccount;
        ResponseEntity<Account> response = accountClient.readByAccountNumber(fundTransferRequest.getFromAccount());
        if(Objects.isNull(response.getBody())){
            log.error("requested account "+fundTransferRequest.getFromAccount()+" is not found on the server");
            throw new ResourceNotFound("requested account not found on the server", GlobalErrorCode.NOT_FOUND);
        }
        fromAccount = response.getBody();
        if(!fromAccount.getAccountStatus().equals("ACTIVE")){
            log.error("Account status is pending or inactive, please update the account status");
            throw new AccountUpdateException("Account status is pending", GlobalErrorCode.NOT_ACCEPTABLE);
        }
        if(fromAccount.getAvailableBalance().compareTo(fundTransferRequest.getAmount()) < 0){
            log.error("Required amount to transfer is not available");
            throw new InsufficientBalance("requested amount is not available", GlobalErrorCode.NOT_ACCEPTABLE);
        }

        Account toAccount;
        response = accountClient.readByAccountNumber(fundTransferRequest.getToAccount());
        if(Objects.isNull(response.getBody())) {
            log.error("requested account "+fundTransferRequest.getToAccount()+" is not found on the server");
            throw new ResourceNotFound("Requested account not found on the server", GlobalErrorCode.NOT_FOUND);
        }
        toAccount = response.getBody();

        String transactionReference = internalTransfer(fromAccount, toAccount, fundTransferRequest.getAmount());

        FundTransfer fundTransfer = FundTransfer.builder()
                .transferType(TransferType.INTERNAL)
                .amount(fundTransferRequest.getAmount())
                .fromAccount(fromAccount.getAccountNumber())
                .transactionReference(transactionReference)
                .status(TransactionStatus.SUCCESS)
                .toAccount(toAccount.getAccountNumber())
                .build();

        fundTransferRepository.save(fundTransfer);
        return FundTransferResponse.builder()
                .transactionReference(transactionReference)
                .message("Fund transfer was successful").build();
    }

    private String internalTransfer(Account fromAccount, Account toAccount, BigDecimal amount){
        fromAccount.setAvailableBalance(fromAccount.getAvailableBalance().subtract(amount));
        accountClient.updateAccount(fromAccount.getAccountNumber(),fromAccount);

        toAccount.setAvailableBalance(toAccount.getAvailableBalance().add(amount));
        accountClient.updateAccount(toAccount.getAccountNumber(), toAccount);

        List<Transaction> transactions = List.of(
                Transaction.builder()
                        .accountNumber(fromAccount.getAccountNumber())
                        .transactionType("INTERNAL_TRANSFER")
                        .amount(amount)
                        .description("Internal fund transfer from "+fromAccount.getAccountNumber()+" to "+toAccount.getAccountNumber())
                        .build(),
                Transaction.builder()
                        .accountNumber(toAccount.getAccountNumber())
                        .transactionType("INTERNAL_TRANSFER")
                        .amount(amount)
                        .description("Internal fund transfer received from: "+fromAccount.getAccountNumber())
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
        return fundTransferMapper.convertToDtoList(fundTransferRepository.findByFromAccount(accountNumber));
    }
}
