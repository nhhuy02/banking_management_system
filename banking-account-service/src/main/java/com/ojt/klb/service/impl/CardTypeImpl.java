package com.ojt.klb.service.impl;

import com.ojt.klb.dto.CardTypeDto;
import com.ojt.klb.mapper.CardTypeMapper;
import com.ojt.klb.model.CardType;
import com.ojt.klb.repository.CardTypeRepository;
import com.ojt.klb.service.CardTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardTypeImpl implements CardTypeService {

    private static final Logger logger = LoggerFactory.getLogger(CardTypeImpl.class);

    private final CardTypeRepository cardTypeRepository;

    private final CardTypeMapper cardTypeMapper;

    public CardTypeImpl(CardTypeRepository cardTypeRepository, CardTypeMapper cardTypeMapper) {
        this.cardTypeRepository = cardTypeRepository;
        this.cardTypeMapper = cardTypeMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Optional<CardTypeDto> createCardType(CardTypeDto cardTypeDto) {
        logger.info("Creating new CardType: {}", cardTypeDto);
        try {
            CardType cardType = cardTypeMapper.toEntity(cardTypeDto);
            cardType = cardTypeRepository.save(cardType);
            CardTypeDto savedCardTypeDto = cardTypeMapper.toCardTypeDto(cardType);
            return Optional.of(savedCardTypeDto);
        } catch (Exception e) {
            logger.error("Error creating CardType: {}", e.getMessage());
            return Optional.empty();
        }
    }


    @Override
    public List<CardTypeDto> getAllCardTypesIsActive() {
        logger.info("Fetching all active CardTypes isActive");
        List<CardType> cardTypes = cardTypeRepository.findByIsActiveTrue();
        return cardTypes.stream()
                .map(cardTypeMapper::toCardTypeDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CardTypeDto> getAllCardTypes() {
        logger.info("Fetching all active CardTypes");
        List<CardType> cardTypes = cardTypeRepository.findAll();
        return cardTypes.stream()
                .map(cardTypeMapper::toCardTypeDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCardType(Long id, CardTypeDto cardTypeDto) {
        logger.info("Updating CardType with id: {}", id);
        Optional<CardType> cardTypeOptional = cardTypeRepository.findById(id);

        if (cardTypeOptional.isPresent()) {
            CardType cardType = cardTypeOptional.get();
            cardType.setCardTypeName(cardTypeDto.getCardTypeName());
            cardType.setNfc(cardTypeDto.getNfc());
            cardType.setDescription(cardTypeDto.getDescription());
            cardType.setIsActive(cardTypeDto.getIsActive());

            cardTypeRepository.save(cardType);
            logger.info("Updated CardType successfully.");
        } else {
            logger.warn("CardType with id {} not found.", id);
        }
    }


}
