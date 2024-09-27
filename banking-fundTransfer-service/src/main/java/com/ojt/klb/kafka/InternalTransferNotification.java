package com.ojt.klb.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternalTransferNotification {
//    private String emailCustomerSend;
//    private String emailCustomerReceive;
//    private Long customerSendId;
//    private Long customerReceiveId;
    private String transactionId;
    private LocalDateTime transactionDate;
    private String senderBankAccount;
    private String receiveBankAccount;
//    private String recipientName;
    private BigDecimal amounts;
    private String description;
    private BigDecimal balanceAccountSend;
    private BigDecimal balanceAccountReceive;
}
