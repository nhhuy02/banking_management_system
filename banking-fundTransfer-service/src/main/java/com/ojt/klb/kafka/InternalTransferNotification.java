package com.ojt.klb.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ojt.klb.model.TransferType;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "ID người gửi tiền")
    private Long customerSendId;
    @Schema(description = "ID người gửi tiền")
    private Long customerReceiveId;
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
    private String bankName;

    public InternalTransferNotification(String email, String email1, Long customerId, String transactionReference, TransferType transferType, LocalDateTime transferredOn, String accountNumber, String accountNumber1, String accountName, BigDecimal amount, String description, ResponseEntity<String> fromAccountBalance, ResponseEntity<String> toAccountBalance) {
    }
}
