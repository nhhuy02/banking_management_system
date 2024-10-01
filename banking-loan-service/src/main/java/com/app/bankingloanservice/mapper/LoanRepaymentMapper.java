package com.app.bankingloanservice.mapper;

import com.app.bankingloanservice.dto.LoanRepaymentResponse;
import com.app.bankingloanservice.entity.LoanRepayment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting LoanRepayment entities to DTOs and vice versa.
 */
@Mapper(componentModel = "spring")
public interface LoanRepaymentMapper {

    @Mapping(source = "loan.loanId", target = "loanId")
    LoanRepaymentResponse toResponse(LoanRepayment loanRepayment);

}