package com.ojt.klb.banking_notification_service.dto.consumer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LoanData {
    private Long customerId;
    private String customerName;
    private String email;
    private Long contractNumber;
    private BigDecimal amounts;
    private LocalDate deadline;
}
