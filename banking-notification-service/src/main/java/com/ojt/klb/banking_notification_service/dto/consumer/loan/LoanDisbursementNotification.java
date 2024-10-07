package com.ojt.klb.banking_notification_service.dto.consumer.loan;

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
public class LoanDisbursementNotification {
    private Long customerId;
    private Long loanId;
    private String customerAccountNumber;
    private String loanContractNo;
    private String customerName;
    private String customerEmail;
    private BigDecimal disbursedAmount;
    private LocalDate disbursementDate;
}
