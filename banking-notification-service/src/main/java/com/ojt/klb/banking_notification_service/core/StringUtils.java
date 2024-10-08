package com.ojt.klb.banking_notification_service.core;

import com.ojt.klb.banking_notification_service.dto.response.ResponseMessage;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;


public class StringUtils {
    public static final String SPACE = " ";
    public static final String EMPTY = "";
    public static final String HYPHEN = "-";
    public static final String COLON = ":";
    public static final String LOAN_APP = "Đơn Đăng ký khoản vay của bạn số: ";
    public static final String LOAN_CONTRACT = "Khoản vay của bạn số HĐ: ";
    public static final String LOAN_DISBURSEMENT = "được giải ngân";
    public static final String LOAN_PAYMENT_REMINDER = "Nhăc nhở thanh toán khoản vay số HĐ:";
    public static final String LOAN_PAYMENT_OVERDUE = "Nhăc nhở thanh toán khoản vay quá hạn số HĐ:";
    public static final String BEFORE = "trước ngày";

    public static boolean stringNotNullOrEmpty(String value) {
        if (value == null || EMPTY.equals(value.trim()))
            return false;

        return true;
    }

    public static String convertVND(BigDecimal amounts) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedAmounts = decimalFormat.format(amounts) + ResponseMessage.VND.statusCodeValue();
        return formattedAmounts;
    }

    public static String convertDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm EEEE dd/MM/yyyy", new Locale("vi", "VN"));
        String formattedDate = dateTime.format(formatter);
        return formattedDate;
    }

    public static long calculateDaysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public static String convertContentIncreaseBalance(String AccountNumber,BigDecimal amounts,BigDecimal balance) {
        String formattedAmounts = convertVND(amounts);
        String formattedBalance = convertVND(balance);
        String content = ResponseMessage.CONTENT_BALANCE.statusCodeValue() + AccountNumber + SPACE + ResponseMessage.INCREASE.statusCodeValue()
                + formattedAmounts + SPACE + ResponseMessage.BALANCE.statusCodeValue() + formattedBalance;
        return content;
    }

    public static String convertContentDecreaseBalance(String AccountNumber,BigDecimal amounts,BigDecimal balance) {
        String formattedAmounts = convertVND(amounts);
        String formattedBalance = convertVND(balance);
        String content = ResponseMessage.CONTENT_BALANCE.statusCodeValue()+ AccountNumber + SPACE + ResponseMessage.DECREASE.statusCodeValue()
                + formattedAmounts + SPACE + ResponseMessage.BALANCE.statusCodeValue() + formattedBalance;
        return content;
    }

    public static String convertContentLoanApplication(Long contractNumber, String status) {
        String content = LOAN_APP + contractNumber.toString()+ HYPHEN+ status;
        return content;
    }

    public static String convertContentLoanDisbursement(String contractNumber, BigDecimal amounts) {
        String content = LOAN_CONTRACT + contractNumber+LOAN_DISBURSEMENT+ COLON+ convertVND(amounts);
        return content;
    }

    public static String convertContentLoanReminder(String contractNumber, LocalDate deadline) {
        String content = LOAN_PAYMENT_REMINDER+ SPACE + contractNumber + SPACE+ BEFORE + SPACE + deadline;
        return content;
    }

    public static String convertContentLoanOverDue(String contractNumber) {
        String content = LOAN_PAYMENT_OVERDUE+ SPACE + contractNumber;
        return content;
    }


}
