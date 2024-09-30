package com.ojt.klb.service;

import com.ojt.klb.dto.CardTypeDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface CardTypeService {
    Optional<CardTypeDto> createCardType(CardTypeDto cardTypeDto);
    List<CardTypeDto> getAllCardTypesIsActive();
    void updateCardType(Long id, CardTypeDto cardTypeDto);
    List<CardTypeDto> getAllCardTypes();
}
