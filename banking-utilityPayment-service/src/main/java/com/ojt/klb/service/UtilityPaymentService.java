package com.ojt.klb.service;

import com.ojt.klb.model.request.UtilityPaymentRequest;
import com.ojt.klb.model.response.UtilityPaymentResponse;


public interface UtilityPaymentService {
    UtilityPaymentResponse utilPayment(UtilityPaymentRequest paymentRequest);
}
