package com.app.bankingloanservice.client.account.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
public class AccountDto {

    private String fullName;

    private String accountName;

    private String accountNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String gender;

    private String email;

    private String phoneNumber;

    private String permanentAddress;

    private String currentAddress;

    private BigDecimal balance;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp openingDate;
}