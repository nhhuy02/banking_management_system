package com.app.bankingloanservice.client.fundtransfer;

import com.app.bankingloanservice.client.fundtransfer.dto.FundTransferRequest;
import com.app.bankingloanservice.client.fundtransfer.dto.FundTransferResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "banking-fundTransfer-service", url = "${app.fund-transfer-client.url}")
public interface FundTransferClient {

    @PostMapping("/internal")
    ResponseEntity<FundTransferResponse> transferFunds(@RequestBody FundTransferRequest fundTransferRequest);

}
