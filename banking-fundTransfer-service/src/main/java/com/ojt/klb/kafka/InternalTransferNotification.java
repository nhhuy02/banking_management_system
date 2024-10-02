package com.ojt.klb.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ojt.klb.model.TransferType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InternalTransferNotification {
    private String emailCustomerSend;
    private String emailCustomerReceive;
    private String transactionId;
    private String transactionType;
    private LocalDateTime transactionDate;
    private String senderBankAccount;
    private String receiveBankAccount;
    private String recipientName;
    private BigDecimal amounts;
    private String description;
    private String balanceAccountSend;
    private String balanceAccountReceive;

    public InternalTransferNotification(String email, String email1, String transactionReference, TransferType transferType, LocalDateTime transferredOn, String accountNumber, String accountNumber1, String accountName, BigDecimal amount, String description, ResponseEntity<String> fromAccountBalance, ResponseEntity<String> toAccountBalance) {
    }
}
