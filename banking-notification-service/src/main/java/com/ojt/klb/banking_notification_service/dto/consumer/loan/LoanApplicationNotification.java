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
public class LoanApplicationNotification {
    private Long customerId;
    private String customerName;
    private String email;
    private String status;
    private Long loanApplicationId;
    private BigDecimal amounts;
    private Integer loanTermMonths;
    private Integer reviewTimeDays;
    private LocalDate submissionDate;
}
