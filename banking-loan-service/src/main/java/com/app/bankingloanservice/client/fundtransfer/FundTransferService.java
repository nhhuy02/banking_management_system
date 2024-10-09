package com.app.bankingloanservice.client.fundtransfer;

import com.app.bankingloanservice.client.fundtransfer.dto.ApiResponse;
import com.app.bankingloanservice.client.fundtransfer.dto.FundTransferRequest;
import com.app.bankingloanservice.client.fundtransfer.dto.FundTransferResponse;
import com.app.bankingloanservice.exception.FundTransferException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FundTransferService {

    private final FundTransferClient fundTransferClient;

    public FundTransferResponse performFundTransfer(FundTransferRequest fundTransferRequest) {
        log.info("Initiating fund transfer for account: {}", fundTransferRequest.getFromAccount());
        try {
            ApiResponse<FundTransferResponse> response = fundTransferClient.internalFundTransfer(fundTransferRequest);

            // Check the results from ApiResponse
            if (!response.isSuccess()) {
                String message = "Fund transfer failed from account number " + fundTransferRequest.getFromAccount() + " to account number " + fundTransferRequest.getToAccount() + " with amount: " + fundTransferRequest.getAmount() + ". No valid response from Fund Transaction Service: {}" + response.getMessage();
                log.error("Fund transfer error: {}", message);
                throw new FundTransferException(message);
            }

            log.info("Fund transfer successful. Transaction reference: {}", response.getData().getTransactionReference());
            return response.getData();
        } catch (Exception e) {
            log.error("Error performing fund transfer for account: {}", fundTransferRequest.getFromAccount(), e);
            throw new FundTransferException(e.getMessage(), e);
        }
    }
}
