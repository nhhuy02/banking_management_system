package com.ojt.klb.model.mapper;

import com.ojt.klb.model.dto.UtilityPaymentDto;
import com.ojt.klb.model.entity.UtilityPayment;
import org.springframework.beans.BeanUtils;

public class UtilityPaymentMapper extends BaseMapper<UtilityPayment, UtilityPaymentDto> {
    @Override
    public UtilityPayment convertToEntity(UtilityPaymentDto dto, Object... args) {
        UtilityPayment entity = new UtilityPayment();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    @Override
    public UtilityPaymentDto convertToDto(UtilityPayment entity, Object... args) {
        UtilityPaymentDto dto = new UtilityPaymentDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}