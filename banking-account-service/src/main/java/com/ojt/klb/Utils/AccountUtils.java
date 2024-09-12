package com.ojt.klb.Utils;

import lombok.RequiredArgsConstructor;

import java.util.Random;

@RequiredArgsConstructor
public class AccountUtils {
    private static final Random RANDOM = new Random();

    public static String generateAccountNumber(){
        return "3420" + generateRandomNumber(8);
    }


    private static String generateRandomNumber(int len) {
        StringBuilder number = new StringBuilder();
        for(int i = 0; i < len; i++){
            int digit = RANDOM.nextInt(10);
            number.append(digit);
        }
        return number.toString();
    }
}
