package com.ojt.klb.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UtilityPaymentResponse {
    private String message;
    private String referenceNumber;
}
