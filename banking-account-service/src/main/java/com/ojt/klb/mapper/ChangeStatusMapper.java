package com.ojt.klb.mapper;

import com.ojt.klb.dto.ChangeStatusDto;
import com.ojt.klb.model.Account;
import org.mapstruct.factory.Mappers;

public interface ChangeStatusMapper {
    ChangeStatusMapper INSTANCE = Mappers.getMapper(ChangeStatusMapper.class);

    ChangeStatusDto toDto(Account account);

    Account toEntity(ChangeStatusDto changeStatusDto);
}
