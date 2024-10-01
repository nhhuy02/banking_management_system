package com.app.bankingloanservice.mapper;

import com.app.bankingloanservice.dto.LoanInterestRateRequest;
import com.app.bankingloanservice.dto.LoanTypeDto;
import com.app.bankingloanservice.entity.LoanType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanTypeMapper {

    public LoanTypeDto toDto(LoanType loanType);

    public LoanType toEntity(LoanTypeDto loanTypeDto);

    public LoanInterestRateRequest toLoanInterestRateCreateDto(LoanType loanType);

}
