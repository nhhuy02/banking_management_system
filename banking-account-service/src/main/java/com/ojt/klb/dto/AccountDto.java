package com.ojt.klb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ojt.klb.model.Account;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
public class AccountDto {

    @JsonIgnore
    private Long id;

    private String fullName;

    private String accountName;

    private Long accountNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String gender;

    private String email;

    private String phoneNumber;

    private String permanentAddress;

    private String currentAddress;

    private BigDecimal balance;

    private Account.Status status ;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp openingDate;
}
