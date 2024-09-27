package com.ojt.klb.utils;

import com.ojt.klb.repository.AccountRepository;
import com.ojt.klb.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;

@Service
public class GenerateUniqueNumber {

    private final AccountRepository accountRepository;
    private final SecureRandom random = new SecureRandom();
    private final CardRepository cardRepository;

    @Autowired
    public GenerateUniqueNumber(AccountRepository accountRepository, CardRepository cardRepository) {
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
    }

    public String generate() {
        String accountNumber;
        do {
            accountNumber = new BigInteger(40, random).toString().substring(0, 10);
        } while (accountRepository.existsByAccountNumber(Long.parseLong(accountNumber)));
        return accountNumber;
    }

    public String generateCardNumber() {
        String cardNumber;
        do {
            cardNumber = new BigInteger(40, random).toString().substring(0, 10);
        } while (cardRepository.existsByCardNumber((cardNumber)));
        return cardNumber;
    }
}
