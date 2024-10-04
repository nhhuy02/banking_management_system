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

    // Loan ID
    private Long loanId;

    // Loan contract number
    private String loanContractNo;

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    // Borrower's account number
    private String customerAccountNumber;

    // Disbursed loan amount
    private BigDecimal disbursedAmount;

    // Transaction reference number
    private String transactionReference;

    // Disbursement date
    private LocalDate disbursementDate;
}
