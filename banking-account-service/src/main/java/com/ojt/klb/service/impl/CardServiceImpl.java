package com.ojt.klb.service.impl;

import com.ojt.klb.model.Account;
import com.ojt.klb.model.Card;
import com.ojt.klb.model.CardType;
import com.ojt.klb.repository.AccountRepository;
import com.ojt.klb.repository.CardRepository;
import com.ojt.klb.repository.CardTypeRepository;
import com.ojt.klb.service.CardService;
import com.ojt.klb.utils.GenerateUniqueNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

@Service
public class CardServiceImpl implements CardService {

    private static final Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);


    private final CardRepository cardRepository;

    private final AccountRepository accountRepository;

    private final CardTypeRepository cardTypeRepository;

    private final GenerateUniqueNumber generateUniqueNumber;

    public CardServiceImpl(CardRepository cardRepository, AccountRepository accountRepository, CardTypeRepository cardTypeRepository, GenerateUniqueNumber generateUniqueNumber) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.cardTypeRepository = cardTypeRepository;
        this.generateUniqueNumber = generateUniqueNumber;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void registerCardNapas(Long accountId) {
        Card card = new Card();

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        card.setAccount(account);

        CardType cardType = cardTypeRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("CardType not found"));
        card.setCardType(cardType);

        card.setCardNumber(generateUniqueNumber.generateCardNumber());
        card.setBalance(BigDecimal.ZERO);
        card.setStatus(Card.Status.active);
        card.setOpenedAt(Timestamp.from(Instant.now()));

        cardRepository.save(card);
        logger.info("Register card Napas successful");
    }
}
