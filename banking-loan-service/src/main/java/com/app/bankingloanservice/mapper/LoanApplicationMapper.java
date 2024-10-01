package com.app.bankingloanservice.mapper;

import com.app.bankingloanservice.dto.LoanApplicationRequest;
import com.app.bankingloanservice.dto.LoanApplicationResponse;
import com.app.bankingloanservice.entity.LoanApplication;
import com.app.bankingloanservice.entity.LoanType;
import com.app.bankingloanservice.service.LoanTypeService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Mapper(componentModel = "spring", imports = {LocalDate.class}, uses = {LoanTypeMapper.class, CollateralMapper.class})
public abstract class LoanApplicationMapper {

    @Autowired
    private LoanTypeService loanTypeService;

    // Mapping LoanApplicationRequest to LoanApplication
    @Mapping(target = "loanType", source = "loanTypeId", qualifiedByName = "getLoanTypeById")  // Get loanTypeId
    @Mapping(target = "applicationStatus", constant = "PENDING")  // Set initial value for application Status
    @Mapping(target = "submissionDate", expression = "java(LocalDate.now())")   // Get the current date for submissionDate
    public abstract LoanApplication toEntity(LoanApplicationRequest dto);

    // Get LoanType from LoanTypeService
    @Named("getLoanTypeById")
    public LoanType getLoanTypeById(Long loanTypeId) {
        return loanTypeService.getLoanTypeById(loanTypeId);
    }

    // Map LoanApplication to LoanApplicationResponse
    @Mapping(target = "loanTypeDto", source = "loanType")
    @Mapping(target = "collateralResponse", source = "collateral")
    public abstract LoanApplicationResponse toResponse(LoanApplication loanApplication);


}
