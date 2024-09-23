package com.ojt.klb.service;

import com.ojt.klb.model.dto.TransactionDto;
import com.ojt.klb.model.response.Response;

import java.math.BigDecimal;

public interface TransactionService {
    public Response handleDeposit(String accountNumber, BigDecimal amount);
    public Response handleWithdraw(String accountNumber, BigDecimal amount);
    public Response internalFundTransfer(TransactionDto request);
}
