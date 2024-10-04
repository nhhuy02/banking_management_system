package com.app.bankingloanservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanDisbursementResponse {

    // Loan contract number
    private String loanContractNo;

    // Borrower's account number
    private String borrowerAccountNumber;

    // Disbursed loan amount
    private BigDecimal disbursedAmount;

    // Transaction reference number
    private String transactionReference;

    // Disbursement date
    private LocalDate disbursementDate;
}
