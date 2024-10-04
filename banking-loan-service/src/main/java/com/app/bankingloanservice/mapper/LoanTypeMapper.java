package com.app.bankingloanservice.mapper;

import com.app.bankingloanservice.dto.LoanInterestRateRequest;
import com.app.bankingloanservice.dto.LoanTypeRequest;
import com.app.bankingloanservice.dto.LoanTypeResponse;
import com.app.bankingloanservice.entity.LoanType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanTypeMapper {

    public LoanTypeResponse toResponse(LoanType loanType);

    public LoanType toEntity(LoanTypeRequest loanTypeRequest);

    public LoanInterestRateRequest toLoanInterestRateCreateDto(LoanType loanType);

}
