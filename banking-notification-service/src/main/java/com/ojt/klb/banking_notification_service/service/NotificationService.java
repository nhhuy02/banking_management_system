package com.ojt.klb.banking_notification_service.service;


import com.ojt.klb.banking_notification_service.dto.NotificationDTO;
import com.ojt.klb.banking_notification_service.dto.Response.ListResponse;
import com.ojt.klb.banking_notification_service.dto.consumer.*;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    void sendMailVerifyOTP(OtpEmailRequestDto otpEmailRequestDto);
    ListResponse<NotificationDTO> findByCustomerId(Long id, Pageable pageable);
    void sendMailPaymentReceipt(TransactionData transactionData);
    void sendMailPaymentReminder(LoanData loanData);
    void sendMailRegister(AccountData accountData);
    void sendMailLoanApplication(LoanDto loanDto);
}
