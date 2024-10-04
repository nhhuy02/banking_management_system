package com.ojt.klb.service;

import com.ojt.klb.model.dto.UtilityPaymentDto;
import com.ojt.klb.model.request.UtilityPaymentRequest;
import com.ojt.klb.model.response.UtilityPaymentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UtilityPaymentService {
    UtilityPaymentResponse utilPayment(UtilityPaymentRequest paymentRequest);
    List<UtilityPaymentDto> readPayments(Pageable pageable);
}
