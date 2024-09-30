package com.ojt.klb.mapper;

import com.ojt.klb.dto.CardTypeDto;
import com.ojt.klb.model.CardType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CardTypeMapper {
    CardTypeMapper INSTANCE = Mappers.getMapper(CardTypeMapper.class);

    CardTypeDto toCardTypeDto(CardType cardType);

    CardType toEntity(CardTypeDto cardTypeDto);
}
