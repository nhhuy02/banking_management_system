package com.ojt.klb.baking_notification_service.dto.consumer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionData {
    @Schema(description = "Email")
    private String email;
    @Schema(description = "ID người gửi tiền")
    private Long customerId;
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
}
