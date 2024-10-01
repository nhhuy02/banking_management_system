package com.app.bankingloanservice.mapper;

import com.app.bankingloanservice.dto.LoanSettlementResponse;
import com.app.bankingloanservice.entity.LoanSettlement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between LoanSettlement and LoanSettlementResponse.
 */
@Mapper(componentModel = "spring")
public interface LoanSettlementMapper {

    @Mapping(source = "loan.loanId", target = "loanId")
    LoanSettlementResponse toResponse(LoanSettlement loanSettlement);

}