package com.ojt.klb.banking_notification_service.service.impl;

import com.ojt.klb.banking_notification_service.core.Status;
import com.ojt.klb.banking_notification_service.core.StringUtils;
import com.ojt.klb.banking_notification_service.dto.NotificationDTO;
import com.ojt.klb.banking_notification_service.dto.Response.ListResponse;
import com.ojt.klb.banking_notification_service.dto.Response.ResponseMessage;
import com.ojt.klb.banking_notification_service.dto.consumer.*;
import com.ojt.klb.banking_notification_service.dto.consumer.account.AccountData;
import com.ojt.klb.banking_notification_service.dto.consumer.loan.LoanDueDate;
import com.ojt.klb.banking_notification_service.dto.consumer.loan.LoanApplicationNotification;
import com.ojt.klb.banking_notification_service.dto.consumer.loan.LoanDisbursementNotification;
import com.ojt.klb.banking_notification_service.dto.consumer.loan.LoanOverdue;
import com.ojt.klb.banking_notification_service.dto.consumer.trans.TransData;
import com.ojt.klb.banking_notification_service.dto.consumer.trans.TransactionInternalData;
import com.ojt.klb.banking_notification_service.entity.Notification;
import com.ojt.klb.banking_notification_service.entity.NotificationTemplate;
import com.ojt.klb.banking_notification_service.repository.NotificationRepository;
import com.ojt.klb.banking_notification_service.repository.NotificationTemplateRepository;
import com.ojt.klb.banking_notification_service.service.MailService;
import com.ojt.klb.banking_notification_service.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
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
    @Qualifier("loanAppKafkaListenerContainerFactory")
    @Autowired
    private ConcurrentKafkaListenerContainerFactory loanAppKafkaListenerContainerFactory;

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
    @KafkaListener(topics = "otp-change-password", groupId = "otp_group", containerFactory = "otpKafkaListenerContainerFactory")
    public void sendMailVerifyPassword(OtpEmailRequestDto otpEmailRequestDto) {
        String email = otpEmailRequestDto.getEmail();
        log.warn("email otp: {}", email);
        NotificationTemplate notificationTemplate = notificationTemplateRepository.getByTemplateName(ResponseMessage.OTP.statusCodeValue());
        String subject =ResponseMessage.NO_REPLY.statusCodeValue()+ notificationTemplate.getSubjectTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", otpEmailRequestDto.getCustomerName());
        variables.put("verificationCode", otpEmailRequestDto.getOtpCode());

        // Gửi email với template
        mailConfig.send(email, subject, "otp_template", variables);
    }

    @Override
    public ListResponse<NotificationDTO> findByCustomerId(Long id, Pageable pageable) {
        Page<NotificationDTO> notificationDTOS = notificationRepository.findNotification(id,pageable);
        return new ListResponse<>(notificationDTOS);
    }

    @Override
    @KafkaListener(topics = "internalTransfer-topic", groupId = "trans_group",containerFactory = "transactionInternalDataConcurrentKafkaListenerContainerFactory")
    public void sendMailPaymentReceipt(TransactionInternalData transactionData) {
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
    @KafkaListener(topics = "transaction-topic", groupId = "trans_group",containerFactory = "transactionDataKafkaListenerContainerFactory")
    public void sendMailTrans(TransData transData) {
        String email = transData.getEmail();
        NotificationTemplate notificationTemplate = notificationTemplateRepository.getByTemplateName(ResponseMessage.BALANCE_CHANGE.statusCodeValue());
        String subject =ResponseMessage.NO_REPLY.statusCodeValue()+ notificationTemplate.getSubjectTemplate();

        Map<String, Object> variables2  = new HashMap<>();
        String content = "",transactionAmount= "",temp = "";

        if(transData.getTransactionType().equalsIgnoreCase("DEPOSIT")){
            content = ResponseMessage.DEPOSIT.statusCodeValue();
            transactionAmount = ResponseMessage.INCREASE.statusCodeValue() + StringUtils.convertVND(transData.getAmount());
            temp= StringUtils.convertContentIncreaseBalance(transData.getAccountNumber(),transData.getAmount(), transData.getBalance());
        } else {
            content = ResponseMessage.WITHDRAWAL.statusCodeValue();
            transactionAmount = ResponseMessage.DECREASE.statusCodeValue() + StringUtils.convertVND(transData.getAmount());
            temp= StringUtils.convertContentDecreaseBalance(transData.getAccountNumber(),transData.getAmount(), transData.getBalance());
        }

        variables2.put("customerName", transData.getCustomerName());
        variables2.put("accountNumber", transData.getAccountNumber());
        variables2.put("balance", StringUtils.convertVND(transData.getBalance()));
        variables2.put("transactionAmount", transactionAmount);
        variables2.put("transactionDate", StringUtils.convertDateTime(transData.getLocalDateTime()));
        variables2.put("content", content);

        Notification notification = new Notification();
        notification.setCreatedAt(LocalDateTime.now());
        notification.setSendDate(LocalDateTime.now());
        notification.setNotificationTemplateId(notificationTemplate.getId());
        notification.setCustomerId(transData.getCustomerId());
        notification.setContent(temp);

        mailConfig.send(email,subject,"balance_change_notification",variables2);

        notificationRepository.save(notification);
    }


    @Override
    @KafkaListener(topics = "loan_application", groupId = "loan_group",containerFactory = "loanAppKafkaListenerContainerFactory")
    public void sendMailLoanApplication(LoanApplicationNotification loanDto) {
        String email = loanDto.getEmail();
        NotificationTemplate notificationTemplate = notificationTemplateRepository.getByTemplateName(ResponseMessage.LOAN.statusCodeValue());
        String subject =ResponseMessage.NO_REPLY.statusCodeValue()+ notificationTemplate.getSubjectTemplate();
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", loanDto.getCustomerName());
        variables.put("applicationNumber", loanDto.getLoanApplicationId());
        variables.put("time", loanDto.getSubmissionDate());
        variables.put("loanTermMonths", loanDto.getLoanTermMonths());
        variables.put("reviewTimeDays", loanDto.getReviewTimeDays());
        variables.put("loanAmount", StringUtils.convertVND(loanDto.getAmounts()));
        String status = "",content = "";
        if (loanDto.getStatus().equalsIgnoreCase(ResponseMessage.PENDING.statusCodeValue())) {
            status = Status.PENDING.getText();
            content = StringUtils.convertContentLoanApplication(loanDto.getLoanApplicationId(),status);
            variables.put("type",ResponseMessage.PENDING.statusCodeValue() );
        }
        if (loanDto.getStatus().equalsIgnoreCase(ResponseMessage.REVIEWING.statusCodeValue())) {
            status = Status.REVIEWING.getText();
            content = StringUtils.convertContentLoanApplication(loanDto.getLoanApplicationId(),status);
//            variables.put("type",ResponseMessage.PENDING.statusCodeValue() );
        }
        if (loanDto.getStatus().equalsIgnoreCase(ResponseMessage.APPROVED.statusCodeValue())) {
            status = Status.APPROVED.getText();
            content = StringUtils.convertContentLoanApplication(loanDto.getLoanApplicationId(),status);
//            variables.put("type",ResponseMessage.PENDING.statusCodeValue() );
        }
        if (loanDto.getStatus().equalsIgnoreCase(ResponseMessage.CANCELLED.statusCodeValue())) {
            status = Status.CANCELLED.getText();
            content = StringUtils.convertContentLoanApplication(loanDto.getLoanApplicationId(),status);
//            variables.put("type",ResponseMessage.PENDING.statusCodeValue() );
        }

        variables.put("status", status);


        Notification notification = new Notification();
        notification.setCreatedAt(LocalDateTime.now());
        notification.setSendDate(LocalDateTime.now());
        notification.setNotificationTemplateId(notificationTemplate.getId());
        notification.setCustomerId(loanDto.getCustomerId());
        notification.setContent(content);

        mailConfig.send(email, subject, "loan_application", variables);
        notificationRepository.save(notification);
    }
    @KafkaListener(topics = "loan-disbursement-notification", groupId = "loan_group",containerFactory = "loanDisbursementNotificationConcurrentKafkaListenerContainerFactory")
    @Override
    public void sendMailLoanDisbursement(LoanDisbursementNotification loanDisbursementNotification) {
        String email = loanDisbursementNotification.getCustomerEmail();
        NotificationTemplate notificationTemplate = notificationTemplateRepository.getByTemplateName(ResponseMessage.LOAN_DISBURSEMENT.statusCodeValue());
        String subject =ResponseMessage.NO_REPLY.statusCodeValue()+ notificationTemplate.getSubjectTemplate();
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", loanDisbursementNotification.getCustomerName());
        variables.put("contractNumber", loanDisbursementNotification.getLoanContractNo());
        variables.put("disbursedAmount", StringUtils.convertVND(loanDisbursementNotification.getDisbursedAmount()));
        variables.put("disbursementTime", loanDisbursementNotification.getDisbursementDate());
        variables.put("bankAccount", loanDisbursementNotification.getCustomerAccountNumber());


        Notification notification = new Notification();
        notification.setCreatedAt(LocalDateTime.now());
        notification.setSendDate(LocalDateTime.now());
        notification.setNotificationTemplateId(notificationTemplate.getId());
        notification.setCustomerId(loanDisbursementNotification.getCustomerId());
        notification.setContent(StringUtils.convertContentLoanDisbursement(loanDisbursementNotification.getLoanContractNo(),loanDisbursementNotification.getDisbursedAmount()));

        mailConfig.send(email, subject, "loan_disbursement", variables);
        notificationRepository.save(notification);
    }

    @Override
    @KafkaListener(topics = "repayment_due_notification", groupId = "loan_group", containerFactory = "loanDataKafkaListenerContainerFactory")
    public void sendMailPaymentReminder(LoanDueDate loanData) {
        String email = loanData.getEmail();
        NotificationTemplate notificationTemplate = notificationTemplateRepository.getByTemplateName(ResponseMessage.PAYMENT_REMINDER.statusCodeValue());
        String subject =ResponseMessage.NO_REPLY.statusCodeValue()+ notificationTemplate.getSubjectTemplate();
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", loanData.getCustomerName());
        variables.put("contractNumber", loanData.getLoanContractNo());
        variables.put("amounts", StringUtils.convertVND(loanData.getAmountDue()));
        variables.put("deadline",loanData.getDueDate());

        Notification notification = new Notification();
        notification.setCreatedAt(LocalDateTime.now());
        notification.setSendDate(LocalDateTime.now());
        notification.setNotificationTemplateId(notificationTemplate.getId());
        notification.setCustomerId(loanData.getCustomerId());
//        notification.setContent(StringUtils.convertContentLoanReminder(loanData.getContractNumber(), loanData.getDueDate()));
        mailConfig.send(email, subject, "payment_reminder", variables);
        notificationRepository.save(notification);
    }

    @Override
    @KafkaListener(topics = "repayment_overdue_notification", groupId = "loan_group", containerFactory = "loanOverdueConcurrentKafkaListenerContainerFactory")
    public void sendMailLoanOverdue(LoanOverdue loanOverdue) {
        String email = loanOverdue.getEmail();
        NotificationTemplate notificationTemplate = notificationTemplateRepository.getByTemplateName(ResponseMessage.LOAN_PAYMENT_OVERDUE.statusCodeValue());
        String subject =ResponseMessage.NO_REPLY.statusCodeValue()+ notificationTemplate.getSubjectTemplate();
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", loanOverdue.getCustomerName());
        variables.put("contractNumber", loanOverdue.getLoanContractNo());
        variables.put("deadline", loanOverdue.getDueDate());
        variables.put("overdue",StringUtils.calculateDaysBetween(loanOverdue.getOverdueDate(),loanOverdue.getDueDate()));
        variables.put("overdueDate", loanOverdue.getOverdueDate());
        variables.put("latePayment", loanOverdue.getLateInterestAmount());
        variables.put("amounts", loanOverdue.getTotalAmountDue());

        Notification notification = new Notification();
        notification.setCreatedAt(LocalDateTime.now());
        notification.setSendDate(LocalDateTime.now());
        notification.setNotificationTemplateId(notificationTemplate.getId());
        notification.setCustomerId(loanOverdue.getCustomerId());
        notification.setContent(StringUtils.convertContentLoanOverDue(loanOverdue.getLoanContractNo()));
        mailConfig.send(email, subject, "loan_payment_overdue", variables);
        notificationRepository.save(notification);
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
