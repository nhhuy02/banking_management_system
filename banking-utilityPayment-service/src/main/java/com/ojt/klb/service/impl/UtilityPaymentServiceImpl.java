package com.ojt.klb.service.impl;

import com.ojt.klb.exception.GlobalErrorCode;
import com.ojt.klb.exception.InsufficientBalance;
import com.ojt.klb.exception.ResourceNotFound;
import com.ojt.klb.external.AccountClient;
import com.ojt.klb.external.TransactionClient;
import com.ojt.klb.model.TransactionStatus;
import com.ojt.klb.model.entity.UtilityAccount;
import com.ojt.klb.model.entity.UtilityPayment;
import com.ojt.klb.model.external.Account;
import com.ojt.klb.model.external.Transaction;
import com.ojt.klb.model.mapper.UtilityPaymentMapper;
import com.ojt.klb.model.request.UtilityPaymentRequest;
import com.ojt.klb.model.response.ApiResponse;
import com.ojt.klb.model.response.UtilityPaymentResponse;
import com.ojt.klb.repository.UtilityAccountRepository;
import com.ojt.klb.repository.UtilityPaymentRepository;
import com.ojt.klb.service.UtilityPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UtilityPaymentServiceImpl implements UtilityPaymentService {
    private final UtilityPaymentRepository utilityPaymentRepository;
    private final TransactionClient transactionClient;
    private final UtilityAccountRepository utilityAccountRepository;
    private final AccountClient accountClient;

    private UtilityPaymentMapper utilityPaymentMapper = new UtilityPaymentMapper();

    @Override
    public UtilityPaymentResponse utilPayment(UtilityPaymentRequest utilityPaymentRequest) {
        String referenceNumber = generateUniqueReferenceNumber();

        Account account;
        ApiResponse<Account> apiResponse = accountClient.getDataAccountNumber(utilityPaymentRequest.getAccount()).getBody();
        if (Objects.isNull(apiResponse) || !apiResponse.isSuccess()) {
            throw new ResourceNotFound("Requested account not found on the server", GlobalErrorCode.NOT_FOUND);
        }
        account = apiResponse.getData();

        if(account.getBalance().compareTo(utilityPaymentRequest.getAmount()) < 0){
            log.error("insufficient balance in the account");
            throw new InsufficientBalance("Insufficient balance in the account");
        }

        Optional<UtilityAccount> utilityAccount = utilityAccountRepository.findById(utilityPaymentRequest.getProviderId());
        if(utilityAccount.isEmpty()){
            throw new ResourceNotFound("Utility account not found", GlobalErrorCode.NOT_FOUND);
        }

        BigDecimal balanceBeforeTransaction = account.getBalance();

        account.setBalance(account.getBalance().subtract(utilityPaymentRequest.getAmount()));
        accountClient.updateAccount(utilityPaymentRequest.getAccount(), account);

        Transaction transaction = Transaction.builder()
                .accountNumber(utilityPaymentRequest.getAccount())
                .transactionType("UTILITY_PAYMENT")
                .amount(utilityPaymentRequest.getAmount().negate())
                .balanceBeforeTransaction(balanceBeforeTransaction)
                .balanceAfterTransaction(account.getBalance())
                .fee(BigDecimal.valueOf(0))
                .description(utilityPaymentRequest.getDescription())
                .build();

        transactionClient.saveUtilityPaymentTransaction(transaction, referenceNumber);

        UtilityPayment utilityPayment = UtilityPayment.builder()
                .referenceNumber(referenceNumber)
                .providerId(utilityPaymentRequest.getProviderId())
                .amount(utilityPaymentRequest.getAmount())
                .account(utilityPaymentRequest.getAccount())
                .status(TransactionStatus.SUCCESS)
                .build();

        utilityPaymentRepository.save(utilityPayment);

        return UtilityPaymentResponse.builder()
                .message("Utility Payment Successfully Processed")
                .referenceNumber(referenceNumber)
                .build();
    }

    private String generateUniqueReferenceNumber() {
        return "ULT" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
    }
}
