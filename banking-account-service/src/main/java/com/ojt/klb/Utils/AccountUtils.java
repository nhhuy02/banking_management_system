package com.ojt.klb.Utils;

import lombok.RequiredArgsConstructor;

import java.security.SecureRandom;

@RequiredArgsConstructor
public class AccountUtils {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generateAccountNumber() {
        return "3420" + generateRandomNumber(8);
    }

    private static String generateRandomNumber(int len) {
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int digit = SECURE_RANDOM.nextInt(10);
            number.append(digit);
        }
        return number.toString();
    }
}
