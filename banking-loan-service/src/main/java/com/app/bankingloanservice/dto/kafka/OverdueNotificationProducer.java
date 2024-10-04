package com.app.bankingloanservice.dto.kafka;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.math.BigDecimal;

@Setter
@Getter
@Builder
public class OverdueNotificationProducer {
    private Long loanRepaymentId;
    private Long accountId;
    private String customerName;
    private String email;
    private LocalDate dueDate;
    private LocalDate overdueDate;
    private BigDecimal lateInterestAmount;
    private BigDecimal totalAmountDue;
}