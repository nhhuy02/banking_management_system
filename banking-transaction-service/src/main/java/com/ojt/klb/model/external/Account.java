package com.ojt.klb.model.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    private String accountId;

    private String accountNumber;

    private String accountType;

    private String accountStatus;

    private LocalDate openingDate;

    private BigDecimal availableBalance;

    private Long userId;
}