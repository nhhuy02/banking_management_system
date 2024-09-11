package com.ctv_it.customer_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerUpdateDto {

    @Size(min = 5, max = 100, message = "{fullName.size}")
    @NotNull(message = "{fullName.notBlank}")
    private String fullName;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    private Gender gender;

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    @Size(min = 5,max = 100, message = "{email.size}")
    @Email
    @NotNull(message = "{email.notBlank}")
    private String email;

    @Size(max = 150, message = "{permanentAddress.size}")
    private String permanentAddress;

    @Size(max = 150, message = "{currentAddress.size}")
    private String currentAddress;

}
