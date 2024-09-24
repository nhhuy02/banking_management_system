package com.ctv_it.customer_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerificationCodeDto {

    @Size(max = 6, message = "{code.size}")
    @NotNull(message = "{code.notNull}")
    private String code;

    @NotNull(message = "{email.notNull}")
    @Email
    private String email;
}
