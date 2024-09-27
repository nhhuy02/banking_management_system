package com.ojt.klb.mapper;

import com.ojt.klb.dto.LoginDto;
import com.ojt.klb.dto.RegisterDto;
import com.ojt.klb.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    LoginDto toLoginDto(User user);

    User toEntity(LoginDto loginDto);
}
