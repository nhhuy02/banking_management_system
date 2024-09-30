package com.ojt.klb.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SavingsAccountResponseDto {

    private String name;

    private String accountName;

    private String email;

    private String phone;

    private BigDecimal balance;

    private String status ;
}
