package com.ojt.klb.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BankAccountDto {
    private Long id;
    private String bankName;
    private String accountName;
    private String accountNumber;
    private BigDecimal balance;
}
