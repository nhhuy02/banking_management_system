package com.ojt.klb.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @Size(min = 10, max = 20, message = "{phoneNumber.size}")
    @NotNull(message = "{phoneNumber.notBlank}")
    private String phoneNumber;

    @Size(min = 3, max = 150, message = "{password.size}")
    @NotNull(message = "{password.notBlank}")
    private String password;
}
