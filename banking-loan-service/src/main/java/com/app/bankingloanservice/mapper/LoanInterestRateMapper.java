package com.app.bankingloanservice.mapper;

import com.app.bankingloanservice.dto.LoanInterestRateRequest;
import com.app.bankingloanservice.dto.LoanInterestRateResponse;
import com.app.bankingloanservice.entity.LoanInterestRate;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class LoanInterestRateMapper {


    public abstract LoanInterestRate toEntity(LoanInterestRateRequest dto);

    @Mapping(source = "loan.loanId", target = "loanId")
    public abstract LoanInterestRateResponse toResponse(LoanInterestRate entity);

}
