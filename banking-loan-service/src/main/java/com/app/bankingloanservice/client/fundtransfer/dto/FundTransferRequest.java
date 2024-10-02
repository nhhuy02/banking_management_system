package com.app.bankingloanservice.client.fundtransfer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundTransferRequest {

    private String fromAccount;

    private String toAccount;

    private BigDecimal amount;

    private String description;
}
