package com.ctv_it.customer_service.mapper;

import com.ctv_it.customer_service.dto.KycDto;
import com.ctv_it.customer_service.dto.KycResponseDto;
import com.ctv_it.customer_service.model.Kyc;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface KycMapper {
    KycMapper INSTANCE = Mappers.getMapper(KycMapper.class);

    KycDto toDto(Kyc kyc);

    Kyc toEntity(KycDto kycDto);

    KycResponseDto toKycResponseDto(Kyc kyc);
}
