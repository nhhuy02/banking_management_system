package com.ctv_it.customer_service.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class CustomersStatusHistoryDto {

    private Long customerId;

    private String status;

    private Instant changedAt;
}
