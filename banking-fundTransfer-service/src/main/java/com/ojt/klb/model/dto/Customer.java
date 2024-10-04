package com.ojt.klb.model.dto;

import java.time.Instant;
import java.time.LocalDate;

public class Customer {
    private Long id;
    private Long accountId;
    private String fullName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String email;
    private String phoneNumber;
    private String permanentAddress;
    private String currentAddress;
    private Instant createdAt;
    private Instant updatedAt;

    public enum Gender {
        MALE, FEMALE, OTHER
    }

}

