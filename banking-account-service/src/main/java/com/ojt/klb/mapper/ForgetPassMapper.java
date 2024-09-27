package com.ojt.klb.mapper;

import com.ojt.klb.dto.ForgetPasswordDto;
import com.ojt.klb.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ForgetPassMapper {
    ForgetPassMapper INSTANCE = Mappers.getMapper(ForgetPassMapper.class);

    ForgetPasswordDto toForgetPass(User user);

    User toEntity(ForgetPasswordDto forgetPasswordDto);
}
