package com.ojt.klb.mapper;

import com.ojt.klb.dto.RegisterDto;
import com.ojt.klb.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RegisterMapper {

    RegisterMapper INSTANCE = Mappers.getMapper(RegisterMapper.class);

    RegisterDto toRegisterDto(User user);

    User toEntity(RegisterDto registerDto);
}
