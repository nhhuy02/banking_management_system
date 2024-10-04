package com.ctv_it.customer_service.dto;

import com.ctv_it.customer_service.model.Kyc;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class  CustomerDto {

//    @JsonIgnore
    private Long customerId;

    private Long accountId;

    @Size(max = 255)
    @NotNull
    private String fullName;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    private Gender gender;

    @Size(max = 255)
    @Email
    @NotNull
    private String email;

    @Size(max = 20)
    @NotNull
    private String phoneNumber;

    @Size(max = 150)
    private String permanentAddress;

    @Size(max = 150)
    private String currentAddress;

    private Kyc kyc;

    private Kyc.VerificationStatus kycStatus;

    private CustomersStatusHistoryDto latestStatus;

    public enum Gender {
        MALE, FEMALE, OTHER
    }
}
