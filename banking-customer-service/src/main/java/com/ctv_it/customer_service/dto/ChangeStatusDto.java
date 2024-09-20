package com.ctv_it.customer_service.dto;

import lombok.Data;

@Data
public class ChangeStatusDto {

    private Long accountId;

    private String status;
}
