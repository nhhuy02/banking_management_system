package com.ojt.klb.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CardRegistrationRequestResponseDto {

    private Long accountId;

    private String name;

    private String email;

    private String phone;

    private String accountNumber;

    private String cardTypeName;

    private String status;

    private LocalDate requestDate;

    private LocalDate reviewDate;

    private String notes;
}