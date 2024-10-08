package com.ojt.klb.banking_notification_service.service;


import com.ojt.klb.banking_notification_service.dto.NotificationDTO;
import com.ojt.klb.banking_notification_service.dto.response.ListResponse;
import com.ojt.klb.banking_notification_service.dto.consumer.*;
import com.ojt.klb.banking_notification_service.dto.consumer.account.AccountData;
import com.ojt.klb.banking_notification_service.dto.consumer.loan.LoanDueDate;
import com.ojt.klb.banking_notification_service.dto.consumer.loan.LoanApplicationNotification;
import com.ojt.klb.banking_notification_service.dto.consumer.loan.LoanDisbursementNotification;
import com.ojt.klb.banking_notification_service.dto.consumer.loan.LoanOverdue;
import com.ojt.klb.banking_notification_service.dto.consumer.trans.TransData;
import com.ojt.klb.banking_notification_service.dto.consumer.trans.TransactionInternalData;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    void sendMailVerifyOTP(OtpEmailRequestDto otpEmailRequestDto);
    ListResponse<NotificationDTO> findByCustomerId(Long id, Pageable pageable);
    void sendMailPaymentReceipt(TransactionInternalData transactionData);
    void sendMailPaymentReminder(LoanDueDate loanData);
    void sendMailRegister(AccountData accountData);
    void sendMailLoanApplication(LoanApplicationNotification loanDto);
    void sendMailTrans(TransData transData);
    void sendMailLoanDisbursement(LoanDisbursementNotification loanDisbursementNotification);
    void sendMailLoanOverdue(LoanOverdue loanOverdue);
    void sendMailVerifyPassword(OtpEmailRequestDto otpEmailRequestDto);
    void sendMailPaymentReceiptExternal(TransactionInternalData transactionData);
}
