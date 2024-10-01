package com.ojt.klb.kafka;

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
    private BigDecimal balanceAccountSend;
    private BigDecimal balanceAccountReceive;

    public InternalTransferNotification(String email, String email1, String transactionReference, TransferType transferType, LocalDateTime transferredOn, String accountNumber, String accountNumber1, String accountName, BigDecimal amount, String description, ResponseEntity<String> stringResponseEntity, ResponseEntity<String> stringResponseEntity1) {
    }
}
