package com.ojt.klb.banking_notification_service.dto.consumer.loan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LoanDueDate {
    private Long customerId;
    private Long loanRepaymentId;
    private Long accountId;
    private String customerName;
    private String email;
    private LocalDate dueDate;
    private BigDecimal amountDue;
}
