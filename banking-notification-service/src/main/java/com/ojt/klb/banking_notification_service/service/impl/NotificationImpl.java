package com.ojt.klb.banking_notification_service.service.impl;

import com.ojt.klb.banking_notification_service.core.StringUtils;
import com.ojt.klb.banking_notification_service.dto.NotificationDTO;
import com.ojt.klb.banking_notification_service.dto.Response.ListResponse;
import com.ojt.klb.banking_notification_service.dto.Response.ResponseMessage;
import com.ojt.klb.banking_notification_service.dto.consumer.AccountData;
import com.ojt.klb.banking_notification_service.dto.consumer.LoanData;
import com.ojt.klb.banking_notification_service.dto.consumer.OtpEmailRequestDto;
import com.ojt.klb.banking_notification_service.dto.consumer.TransactionData;
import com.ojt.klb.banking_notification_service.entity.Notification;
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

import java.time.LocalDateTime;
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
    public void sendMailVerifyOTP(OtpEmailRequestDto customerData) {
        String email = customerData.getEmail();
        log.warn("email otp: {}", email);
        NotificationTemplate notificationTemplate = notificationTemplateRepository.getByTemplateName(ResponseMessage.OTP.statusCodeValue());
        String subject =ResponseMessage.NO_REPLY.statusCodeValue()+ notificationTemplate.getSubjectTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerData.getCustomerName());
        variables.put("verificationCode", customerData.getOtpCode());

        // Gửi email với template
         mailConfig.send(email, subject, "otp_template", variables);

    }

    @Override
    public ListResponse<NotificationDTO> findByCustomerId(Long id, Pageable pageable) {
        Page<NotificationDTO> notificationDTOS = notificationRepository.findNotification(id,pageable);
        return new ListResponse<>(notificationDTOS);
    }

    @Override
    @KafkaListener(topics = "internalTransfer-topic", groupId = "trans_group",containerFactory = "transactionDataKafkaListenerContainerFactory")
    public void sendMailPaymentReceipt(TransactionData transactionData) {
        String emailCustomerSend = transactionData.getEmailCustomerSend();
        log.warn("email trans: {}", emailCustomerSend);
        NotificationTemplate notificationTemplate1 = notificationTemplateRepository.getByTemplateName(ResponseMessage.PAYMENT_RECEIPT.statusCodeValue());
        String subject1 =ResponseMessage.NO_REPLY.statusCodeValue()+ notificationTemplate1.getSubjectTemplate();

        Notification notification1 = new Notification();
        notification1.setCreatedAt(LocalDateTime.now());
        notification1.setSendDate(LocalDateTime.now());
        notification1.setNotificationTemplateId(notificationTemplate1.getId());
        notification1.setCustomerId(transactionData.getCustomerReceiveId());
        notification1.setContent(StringUtils.convertContentDecreaseBalance(transactionData.getSenderBankAccount(),transactionData.getAmounts(), transactionData.getBalanceAccountReceive()));

        Map<String, Object> variables1  = new HashMap<>();
        variables1.put("transactionDate", StringUtils.convertDateTime(transactionData.getTransactionDate()));
        variables1.put("transId", transactionData.getTransactionId());
        variables1.put("sendAccount", transactionData.getSenderBankAccount());
        variables1.put("receiveAccount", transactionData.getReceiveBankAccount());
        variables1.put("recipientName", transactionData.getRecipientName());
        variables1.put("amount", StringUtils.convertVND(transactionData.getAmounts()));
        variables1.put("content", transactionData.getDescription());

        mailConfig.send(emailCustomerSend, subject1, "payment_receipt", variables1);
        notificationRepository.save(notification1);

        String emailCustomerReceive = transactionData.getEmailCustomerReceive();
        NotificationTemplate notificationTemplate2 = notificationTemplateRepository.getByTemplateName(ResponseMessage.BALANCE_CHANGE.statusCodeValue());
        String subject2 =ResponseMessage.NO_REPLY.statusCodeValue()+ notificationTemplate2.getSubjectTemplate();

        Notification notification2 = new Notification();
        notification2.setCreatedAt(LocalDateTime.now());
        notification2.setSendDate(LocalDateTime.now());
        notification2.setNotificationTemplateId(notificationTemplate2.getId());
        notification2.setCustomerId(transactionData.getCustomerReceiveId());
        notification2.setContent(StringUtils.convertContentIncreaseBalance(transactionData.getReceiveBankAccount(),transactionData.getAmounts(), transactionData.getBalanceAccountReceive()));

        Map<String, Object> variables2  = new HashMap<>();
        variables2.put("customerName", transactionData.getRecipientName());
        variables2.put("accountNumber", transactionData.getReceiveBankAccount());
        variables2.put("balance", StringUtils.convertVND(transactionData.getBalanceAccountReceive()));
        variables2.put("transactionAmount", ResponseMessage.INCREASE.statusCodeValue() + StringUtils.convertVND(transactionData.getAmounts()));
        variables2.put("transactionDate", StringUtils.convertDateTime(transactionData.getTransactionDate()));
        variables2.put("content", transactionData.getDescription());

        mailConfig.send(emailCustomerReceive,subject2,"balance_change_notification",variables2);
        notificationRepository.save(notification2);

    }

    @Override
    @KafkaListener(topics = "loan-topic", groupId = "group_id")
    public void sendMailPaymentReminder(LoanData loanData) {
        String email = loanData.getEmail();
        NotificationTemplate notificationTemplate = notificationTemplateRepository.getByTemplateName(ResponseMessage.PAYMENT_REMINDER.statusCodeValue());
        String subject =ResponseMessage.NO_REPLY.statusCodeValue()+ notificationTemplate.getSubjectTemplate();
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", loanData.getCustomerName());
        variables.put("contractNumber", loanData.getContractNumber());
        variables.put("amounts", StringUtils.convertVND(loanData.getAmounts()));
        variables.put("deadline",StringUtils.convertDateTime(loanData.getDeadline()));
         mailConfig.send(email, subject, "payment_reminder", variables);
    }

    @Override
    @KafkaListener(topics = "data-account-topic", groupId = "account_group", containerFactory = "accountDataKafkaListenerContainerFactory")
    public void sendMailRegister(AccountData accountData) {
        String email = accountData.getEmail();
        log.warn("email register: {}", email);
        NotificationTemplate notificationTemplate = notificationTemplateRepository.getByTemplateName(ResponseMessage.REGISTER.statusCodeValue());
        String subject =ResponseMessage.NO_REPLY.statusCodeValue()+ notificationTemplate.getSubjectTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", accountData.getCustomerName());
        variables.put("accountNumber", accountData.getAccountNumber());
        variables.put("accountType", accountData.getAccountName());
        variables.put("phoneNumber", accountData.getPhoneNumber());

         mailConfig.send(email, subject, "account_creation_success", variables);
    }

}
