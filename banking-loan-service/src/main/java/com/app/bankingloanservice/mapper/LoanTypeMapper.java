package com.app.bankingloanservice.mapper;

import com.app.bankingloanservice.dto.LoanTypeDto;
import com.app.bankingloanservice.entity.LoanType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanTypeMapper {

    LoanTypeDto toDto(LoanType loanType);

    LoanType toEntity(LoanTypeDto loanTypeDto);
}
