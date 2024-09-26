package com.ojt.klb.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {
    private Long accountId;

    private String accountNumber;

    @NotBlank(message = "Account type cannot be blank")
    private String accountType;

    private String accountStatus;

    private BigDecimal availableBalance;

//    private Long userId;
}
