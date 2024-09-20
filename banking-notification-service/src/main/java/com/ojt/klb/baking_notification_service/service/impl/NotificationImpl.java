package com.ojt.klb.baking_notification_service.service.impl;

import com.ojt.klb.baking_notification_service.dto.consumer.CustomerData;
import com.ojt.klb.baking_notification_service.dto.NotificationDTO;
import com.ojt.klb.baking_notification_service.dto.Response.ListResponse;
import com.ojt.klb.baking_notification_service.dto.Response.ResponseMessage;
import com.ojt.klb.baking_notification_service.dto.consumer.LoanData;
import com.ojt.klb.baking_notification_service.dto.consumer.TransactionData;
import com.ojt.klb.baking_notification_service.entity.NotificationTemplate;
import com.ojt.klb.baking_notification_service.repository.NotificationRepository;
import com.ojt.klb.baking_notification_service.repository.NotificationTemplateRepository;
import com.ojt.klb.baking_notification_service.service.MailService;
import com.ojt.klb.baking_notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class NotificationImpl implements NotificationService {

    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private MailService mailConfig;
    @Autowired
    NotificationTemplateRepository notificationTemplateRepository;
    @Autowired
    NotificationRepository notificationRepository;

    @Override
    @KafkaListener(topics = "customer-otp-topic", groupId = "group_id")
    public String sendMailVerifyOTP(CustomerData customerData) {
        String email = customerData.getEmail();
        NotificationTemplate notificationTemplate = notificationTemplateRepository.getByTemplateName(ResponseMessage.REGISTER.statusCodeValue());
        String subject =ResponseMessage.NO_REPLY.statusCodeValue()+ notificationTemplate.getSubjectTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerData.getCustomerName());
        variables.put("verificationCode", customerData.getOtpCode());

        // Gửi email với template
        return mailConfig.send(email, subject, "otp_template", variables);

    }

    @Override
    public ListResponse<NotificationDTO> findByCustomerId(Long id, Pageable pageable) {
        Page<NotificationDTO> notificationDTOS = notificationRepository.findNotification(id,pageable);
        return new ListResponse<>(notificationDTOS);
    }

    @Override
    @KafkaListener(topics = "transaction-topic", groupId = "group_id")
    public String sendMailPaymentReceipt(TransactionData transactionData) {
        String email = transactionData.getEmail();
        NotificationTemplate notificationTemplate = notificationTemplateRepository.getByTemplateName(ResponseMessage.PAYMENT_RECEIPT.statusCodeValue());
        String subject =ResponseMessage.NO_REPLY.statusCodeValue()+ notificationTemplate.getSubjectTemplate();

        Map<String, Object> variables = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm EEEE dd/MM/yyyy", new Locale("vi", "VN"));

        String formattedDate = transactionData.getTransactionDate().format(formatter);
        variables.put("transactionDate", formattedDate);
        variables.put("transId", transactionData.getTransactionId());
        variables.put("sendAccount", transactionData.getSenderBankAccount());
        variables.put("receiveAccount", transactionData.getReceiveBankAccount());
        variables.put("recipientName", transactionData.getRecipientName());
//        variables.put("bankName", transactionData.get);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        String formattedAmounts = decimalFormat.format(transactionData.getAmounts());
        variables.put("amount", formattedAmounts +ResponseMessage.VND.statusCodeValue());
        variables.put("content", transactionData.getDescription());
        // Gửi email với template
        return mailConfig.send(email, subject, "payment_receipt", variables);
    }

    @Override
    @KafkaListener(topics = "loan-topic", groupId = "group_id")
    public String sendMailPaymentReminder(LoanData loanData) {
        String email = loanData.getEmail();
        NotificationTemplate notificationTemplate = notificationTemplateRepository.getByTemplateName(ResponseMessage.PAYMENT_REMINDER.statusCodeValue());
        String subject =ResponseMessage.NO_REPLY.statusCodeValue()+ notificationTemplate.getSubjectTemplate();
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", loanData.getCustomerName());
        variables.put("contractNumber", loanData.getContractNumber());

        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        String formattedAmounts = decimalFormat.format(loanData.getAmounts());
        variables.put("amounts", formattedAmounts+ResponseMessage.VND.statusCodeValue());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm EEEE dd/MM/yyyy", new Locale("vi", "VN"));

        String formattedDate = loanData.getDeadline().format(formatter);
        variables.put("deadline",formattedDate);
        return mailConfig.send(email, subject, "payment_reminder", variables);
    }

}
