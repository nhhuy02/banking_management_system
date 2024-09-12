package com.ctv_it.customer_service.mapper;

import com.ctv_it.customer_service.dto.VerificationCodeDto;
import com.ctv_it.customer_service.model.Customer;
import com.ctv_it.customer_service.model.VerificationCode;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VerificationCodeMapper {

    VerificationCodeMapper INSTANCE = Mappers.getMapper(VerificationCodeMapper.class);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "customer.email", target = "email")
    VerificationCodeDto toDto(VerificationCode verificationCode);

    @Mapping(source = "customerId", target = "customer.id")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "email", target = "customer.email")
    VerificationCode toEntity(VerificationCodeDto verificationCodeDto);
}

