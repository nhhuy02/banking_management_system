package com.ojt.klb.banking_notification_service.service.impl;

import com.ojt.klb.banking_notification_service.core.StringUtils;
import com.ojt.klb.banking_notification_service.dto.NotificationDTO;
import com.ojt.klb.banking_notification_service.dto.Response.ListResponse;
import com.ojt.klb.banking_notification_service.dto.Response.ResponseMessage;
import com.ojt.klb.banking_notification_service.dto.consumer.AccountData;
import com.ojt.klb.banking_notification_service.dto.consumer.LoanData;
import com.ojt.klb.banking_notification_service.dto.consumer.OtpEmailRequestDto;
import com.ojt.klb.banking_notification_service.dto.consumer.TransactionData;
import com.ojt.klb.banking_notification_service.entity.NotificationTemplate;
import com.ojt.klb.banking_notification_service.repository.NotificationRepository;
import com.ojt.klb.banking_notification_service.repository.NotificationTemplateRepository;
import com.ojt.klb.banking_notification_service.service.MailService;
import com.ojt.klb.banking_notification_service.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
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
    @KafkaListener(topics = "otp-email-topic", groupId = "otp_group", containerFactory = "otpKafkaListenerContainerFactory")
    public String sendMailVerifyOTP(OtpEmailRequestDto customerData) {
        String email = customerData.getEmail();
        log.warn("email otp: {}", email);
        NotificationTemplate notificationTemplate = notificationTemplateRepository.getByTemplateName(ResponseMessage.OTP.statusCodeValue());
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
    @KafkaListener(topics = "transaction-topic", groupId = "trans_group",containerFactory = "transactionDataKafkaListenerContainerFactory")
    public String sendMailPaymentReceipt(TransactionData transactionData) {
        String email = transactionData.getEmail();
        log.warn("email trans: {}", email);
        NotificationTemplate notificationTemplate = notificationTemplateRepository.getByTemplateName(ResponseMessage.PAYMENT_RECEIPT.statusCodeValue());
        String subject =ResponseMessage.NO_REPLY.statusCodeValue()+ notificationTemplate.getSubjectTemplate();

        Map<String, Object> variables = new HashMap<>();

        variables.put("transactionDate", StringUtils.convertDateTime(transactionData.getTransactionDate()));
        variables.put("transId", transactionData.getTransactionId());
        variables.put("sendAccount", transactionData.getSenderBankAccount());
        variables.put("receiveAccount", transactionData.getReceiveBankAccount());
        variables.put("recipientName", transactionData.getRecipientName());
        variables.put("amount", StringUtils.convertVND(transactionData.getAmounts()));
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
        variables.put("amounts", StringUtils.convertVND(loanData.getAmounts()));
        variables.put("deadline",StringUtils.convertDateTime(loanData.getDeadline()));
        return mailConfig.send(email, subject, "payment_reminder", variables);
    }

    @Override
    @KafkaListener(topics = "data-account-topic", groupId = "account_group", containerFactory = "accountDataKafkaListenerContainerFactory")
    public String sendMailRegister(AccountData accountData) {
        String email = accountData.getEmail();
        log.warn("email register: {}", email);
        NotificationTemplate notificationTemplate = notificationTemplateRepository.getByTemplateName(ResponseMessage.REGISTER.statusCodeValue());
        String subject =ResponseMessage.NO_REPLY.statusCodeValue()+ notificationTemplate.getSubjectTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", accountData.getCustomerName());
        variables.put("accountNumber", accountData.getAccountNumber());
        variables.put("accountType", accountData.getAccountName());
        variables.put("phoneNumber", accountData.getPhoneNumber());

        return mailConfig.send(email, subject, "account_creation_success", variables);
    }

}
