package com.ctv_it.customer_service.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class KycResponseDto {

    private String verificationStatus;

    private Instant verificationDate;

    private String documentType;

    private String documentNumber;

    private String documentImageUrl;
}
