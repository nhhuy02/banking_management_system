package com.ojt.klb.external;

import com.ojt.klb.model.request.UtilityPaymentRequest;
import com.ojt.klb.model.response.UtilityPaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "banking-transaction-service", url = "http://localhost:8070/api/v1/transactions")
public interface TransactionClient {

    @PostMapping("/util-payment")
    UtilityPaymentResponse utilityPayment(@RequestBody UtilityPaymentRequest paymentRequest);
}
