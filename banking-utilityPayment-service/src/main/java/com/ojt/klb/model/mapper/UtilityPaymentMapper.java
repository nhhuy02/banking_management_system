package com.ojt.klb.model.mapper;

import com.ojt.klb.model.dto.UtilityPaymentDto;
import com.ojt.klb.model.entity.UtilityAccount;
import org.springframework.beans.BeanUtils;

public class UtilityPaymentMapper extends BaseMapper<UtilityAccount, UtilityPaymentDto> {
    @Override
    public UtilityAccount convertToEntity(UtilityPaymentDto dto, Object... args) {
        UtilityAccount entity = new UtilityAccount();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    @Override
    public UtilityPaymentDto convertToDto(UtilityAccount entity, Object... args) {
        UtilityPaymentDto dto = new UtilityPaymentDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}