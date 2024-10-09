package com.ctv_it.customer_service.mapper;

import com.ctv_it.customer_service.dto.VerificationCodeRequestDto;
import com.ctv_it.customer_service.model.VerificationCode;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VerificationCodeMapper {

    @Mapping(source = "customer.email", target = "email")
    VerificationCodeRequestDto toDto(VerificationCode verificationCode);

}

