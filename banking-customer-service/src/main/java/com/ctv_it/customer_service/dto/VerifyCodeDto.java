package com.ctv_it.customer_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerifyCodeDto {
    @Size(max = 6, message = "{code.size}")
    @NotNull(message = "{code.notNull}")
    private String code;
}
