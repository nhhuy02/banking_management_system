package com.ojt.klb.service;

import com.ojt.klb.model.dto.FundTransferDto;
import com.ojt.klb.model.request.FundTransferRequest;
import com.ojt.klb.model.response.FundTransferResponse;

import java.util.List;

public interface FundTransferService {
    FundTransferResponse fundTransfer(FundTransferRequest fundTransferRequest);
    FundTransferDto getTransferDetailsFromReferenceNumber(String referenceNumber);
    List<FundTransferDto> getAllTransferByAccountNumber(String accountNumber);
}
