package com.ojt.klb.model.external;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    private String fullName;

    private Long customerId;

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

    private Account.Status status ;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp openingDate;

    public enum Status {
        active,
        suspended,
        closed
    }

}
