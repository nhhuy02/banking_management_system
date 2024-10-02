package com.ojt.klb.banking_notification_service.dto.consumer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TransactionData {
    private String transactionType;
    @Schema(description = "Email người chuyển")
    private String emailCustomerSend;
    @Schema(description = "Email người nhận")
    private String emailCustomerReceive;
    @Schema(description = "ID người gửi tiền")
    private Long customerSendId;
    @Schema(description = "ID người gửi tiền")
    private Long customerReceiveId;
    @Schema(description = "Số lệnh giao dịch")
    private Long transactionId;
    @Schema(description = "Ngày giờ giao dịch")
    private LocalDateTime transactionDate;
    @Schema(description = "Tài khoản nguồn")
    private String senderBankAccount;
    @Schema(description = "Tài khoản người nhận")
    private String receiveBankAccount;
    @Schema(description = "Tên người nhận")
    private String recipientName;
    @Schema(description = "Số tiền")
    private BigDecimal amounts;
    @Schema(description = "Nội dung chuyển khoản")
    private String description;
    @Schema(description = "Số dư tk chuyển")
    private BigDecimal balanceAccountSend;
    @Schema(description = "Số dư tài khoản nhận")
    private BigDecimal balanceAccountReceive;
}
