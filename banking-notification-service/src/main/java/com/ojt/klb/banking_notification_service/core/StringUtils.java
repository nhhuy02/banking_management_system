package com.ojt.klb.banking_notification_service.core;

import com.ojt.klb.banking_notification_service.dto.Response.ResponseMessage;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class StringUtils {
    public static final String SPACE = " ";
    public static final String EMPTY = "";
    public static final String HYPHEN = "-";
    public static final String LOAN_APP = "Đơn Đăng ký khoản vay của bạn số: ";
    public static final String LOAN_CONTRACT = "Khoản vay của bạn số HĐ: ";
    public static final String LOAN_DISBURSEMENT = "được giải ngân";
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
        String content = LOAN_CONTRACT + contractNumber+LOAN_DISBURSEMENT+ HYPHEN+ convertVND(amounts);
        return content;
    }

    public static String convertContentLoanReminder(Long contractNumber, LocalDate deadline) {
        String content = "Nhăc nhở thanh toán khoản vay số HĐ: " + contractNumber.toString()+ SPACE+ "trước ngày "+ deadline;
        return content;
    }


}
