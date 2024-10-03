package com.ojt.klb.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Data
@Builder
@Table(name = "bank_account")
public class BankAccount{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bankName;
    private String accountName;
    private String accountNumber;
    private BigDecimal balance;
}
