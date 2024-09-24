package com.ojt.klb.banking_notification_service.core;

import com.ojt.klb.banking_notification_service.dto.Response.ResponseMessage;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class StringUtils {
    public static final String SPACE = " ";
    public static final String EMPTY = "";
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


}
