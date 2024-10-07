package com.ojt.klb.banking_notification_service.dto.consumer.loan;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanOverdue {
    private Long loanRepaymentId;
    private String loanContractNo;
    private Long accountId;
    private Long customerId;
    private String customerName;
    private String email;
    private LocalDate dueDate;
    private LocalDate overdueDate;
    private BigDecimal lateInterestAmount;
    private BigDecimal totalAmountDue;
}