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
    private String loanContractNo;
    private Long accountId;
    private Long customerId;
    private String customerName;
    private String email;
    private LocalDate dueDate;
    private BigDecimal amountDue;
}