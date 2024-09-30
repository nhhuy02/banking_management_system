package com.ojt.klb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ForgetPasswordDto {

    @NotBlank(message = "{newPassword.notBlank}")
    @Size(min = 6,max = 150, message = "{newPassword.size}")
    private String newPassword;
}
