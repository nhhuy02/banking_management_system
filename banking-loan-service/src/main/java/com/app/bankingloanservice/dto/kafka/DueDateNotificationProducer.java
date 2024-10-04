package com.app.bankingloanservice.dto.kafka;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Builder
public class DueDateNotificationProducer {
    private Long loanRepaymentId;
    private Long accountId;
    private String customerName;
    private String email;
    private LocalDate dueDate;
    private BigDecimal amountDue;
}