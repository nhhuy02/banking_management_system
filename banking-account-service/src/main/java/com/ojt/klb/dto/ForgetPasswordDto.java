package com.ojt.klb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ForgetPasswordDto {

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 15, message = "Phone number size {min} to {max}")
    private String phoneNumber;
}
