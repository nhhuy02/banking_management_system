package com.ojt.klb.baking_notification_service.service;

import com.ojt.klb.baking_notification_service.dto.consumer.CustomerData;
import com.ojt.klb.baking_notification_service.dto.NotificationDTO;
import com.ojt.klb.baking_notification_service.dto.Response.ListResponse;
import com.ojt.klb.baking_notification_service.dto.consumer.LoanData;
import com.ojt.klb.baking_notification_service.dto.consumer.TransactionData;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    String sendMailVerifyOTP(CustomerData customerData);
    ListResponse<NotificationDTO> findByCustomerId(Long id, Pageable pageable);
    String sendMailPaymentReceipt(TransactionData transactionData);
    String sendMailPaymentReminder(LoanData loanData);
}
