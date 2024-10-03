package com.app.bankingloanservice.client.fundtransfer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundTransferResponse {

    private String transactionReference;

    private String message;
}
