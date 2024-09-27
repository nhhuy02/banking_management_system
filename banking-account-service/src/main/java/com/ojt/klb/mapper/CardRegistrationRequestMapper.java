package com.ojt.klb.mapper;

import com.ojt.klb.dto.CardRegistrationRequestDto;
import com.ojt.klb.model.CardRegistrationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CardRegistrationRequestMapper {
    CardRegistrationRequestDto INSTANCE = Mappers.getMapper(CardRegistrationRequestDto.class);

    CardRegistrationRequest toEntity(CardRegistrationRequestDto cardRegistrationRequestDto);
}
