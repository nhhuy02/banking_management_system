package com.ojt.klb.model.mapper;

import com.ojt.klb.model.dto.TransactionDto;
import com.ojt.klb.model.entity.Transaction;
import org.springframework.beans.BeanUtil;

import java.util.Objects;

public class TransactionMapper extends BaseMapper<Transaction, TransactionDto> {

    @Override
    public Transaction convertToEntity(TransactionDto dto, Object... args) {

        Transaction transaction = new Transaction();
        if(!Objects.isNull(dto)){
            BeanUtil.copyProperties(dto, transaction);
        }
        return transaction;
    }

    @Override
    public TransactionDto convertToDto(Transaction entity, Object... args) {

        TransactionDto internalTransferDto = new TransactionDto();
        if(!Objects.isNull(entity)) {
            BeanUtil.copyProperties(entity, internalTransferDto);
        }
        return internalTransferDto;    }
}
