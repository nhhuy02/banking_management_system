package com.ctv_it.customer_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;
@Data
public class KycDto {

    @JsonIgnore
    private Long customerId;

    @JsonIgnore
    private String verificationStatus;

    @JsonIgnore
    private Instant verificationDate;

    @NotNull
    private String documentType;

    @NotBlank(message = "{document_number.notBlank}")
    @Size(min = 10,max = 20, message = "{document_number.size}")
    private String documentNumber;

    @JsonIgnore
    private String documentImageUrl;
}
