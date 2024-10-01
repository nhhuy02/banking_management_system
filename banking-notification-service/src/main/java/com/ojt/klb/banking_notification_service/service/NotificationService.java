package com.ojt.klb.banking_notification_service.service;


import com.ojt.klb.banking_notification_service.dto.NotificationDTO;
import com.ojt.klb.banking_notification_service.dto.Response.ListResponse;
import com.ojt.klb.banking_notification_service.dto.consumer.AccountData;
import com.ojt.klb.banking_notification_service.dto.consumer.LoanData;
import com.ojt.klb.banking_notification_service.dto.consumer.OtpEmailRequestDto;
import com.ojt.klb.banking_notification_service.dto.consumer.TransactionData;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    String sendMailVerifyOTP(OtpEmailRequestDto otpEmailRequestDto);
    ListResponse<NotificationDTO> findByCustomerId(Long id, Pageable pageable);
    String sendMailPaymentReceipt(TransactionData transactionData);
    String sendMailPaymentReminder(LoanData loanData);
    String sendMailRegister(AccountData accountData);
}
