package com.ojt.klb.model.mapper;

import com.ojt.klb.model.dto.BankAccountDto;
import com.ojt.klb.model.entity.BankAccount;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

public class BankAccountMapper extends BaseMapper<BankAccount, BankAccountDto> {


    @Override
    public BankAccount convertToEntity(BankAccountDto dto, Object... args) {
        BankAccount bankAccount = new BankAccount();
        if(!Objects.isNull(dto)){
            BeanUtils.copyProperties(dto, bankAccount);
        }
        return bankAccount;
    }

    @Override
    public BankAccountDto convertToDto(BankAccount entity, Object... args) {

        BankAccountDto bankAccountDto = new BankAccountDto();
        if(!Objects.isNull(entity)) {
            BeanUtils.copyProperties(entity, bankAccountDto);
        }
        return bankAccountDto;
    }
}

