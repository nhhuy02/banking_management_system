package com.ojt.klb.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    private String type;

    @Column(nullable = false)
    private BigDecimal accountingBalance = BigDecimal.ZERO;

    @Column(length = 3) // assuming currency code like "USD"
    private String currency;

    @Column(nullable = false)
    private BigDecimal availableBalance = BigDecimal.ZERO;

    @Column(nullable = false)
    private String status = "active"; // Default to "active"

    @Column(length = 7, unique = true, nullable = false)
    private String accountNumber;

    private String branch;

    @Column(nullable = false, updatable = false) // prevent updates after creation
    private LocalDate openingDate = LocalDate.now(); // Automatically set to current date

    // Optional: Constructor with automatic date setting
    public Account(Long customerId, String type, String currency, String branch, String accountNumber) {
        this.customerId = customerId;
        this.type = type;
        this.currency = currency;
        this.branch = branch;
        this.accountNumber = accountNumber;
        this.openingDate = LocalDate.now(); // Automatically set to current date
    }
}
