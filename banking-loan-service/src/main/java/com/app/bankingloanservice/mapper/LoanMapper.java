package com.app.bankingloanservice.mapper;

import com.app.bankingloanservice.dto.LoanRequest;
import com.app.bankingloanservice.dto.LoanResponse;
import com.app.bankingloanservice.entity.Loan;
import com.app.bankingloanservice.entity.LoanApplication;
import com.app.bankingloanservice.entity.LoanType;
import org.mapstruct.*;

import java.time.LocalDate;

@Mapper(componentModel = "spring", uses = {
        LoanInterestRateMapper.class,
        LoanTypeMapper.class,
        LoanApplicationMapper.class,
        LoanRepaymentMapper.class,
        LoanSettlementMapper.class})
public abstract class LoanMapper {

    @Mapping(target = "loanContractNo", expression = "java(loanContractNo)")
    @Mapping(target = "customerConfirmationStatus", constant = "PENDING")
    @Mapping(target = "maturityDate", expression = "java(maturityDate)")
    @Mapping(target = "loanAmount", source = "desiredLoanAmount")
    @Mapping(target = "loanTermMonths", source = "desiredLoanTermMonths")
    @Mapping(target = "disbursementDate", source = "desiredDisbursementDate")
    @Mapping(target = "renewalCount", constant = "0")
    @Mapping(target = "remainingBalance", source = "desiredLoanAmount")
    @Mapping(target = "totalPaidAmount", constant = "0")
    @Mapping(target = "isBadDebt", constant = "false")
    @Mapping(target = "debtClassification", constant = "NORMAL")
    @Mapping(target = "status", constant = "PENDING")
    public abstract Loan loanApplicationToLoan(LoanApplication loanApplication, @Context String loanContractNo, @Context LocalDate maturityDate);


    /**
     * Maps LoanRequest to Loan entity.
     *
     * @param loanRequest The DTO containing loan creation data.
     * @return The mapped Loan entity.
     */
    @Mapping(target = "loanApplication", expression = "java(loanApplication)")
    @Mapping(target = "loanContractNo", expression = "java(loanContractNo)")
    @Mapping(target = "customerConfirmationStatus", constant = "PENDING")
    @Mapping(target = "maturityDate", expression = "java(maturityDate)")
    @Mapping(target = "loanType", expression = "java(loanType)")
    @Mapping(target = "remainingBalance", source = "loanAmount")
    @Mapping(target = "totalPaidAmount", constant = "0")
    @Mapping(target = "renewalCount", constant = "0")
    @Mapping(target = "isBadDebt", constant = "false")
    @Mapping(target = "debtClassification", constant = "NORMAL")
    @Mapping(target = "status", constant = "PENDING")
    public abstract Loan toEntity(LoanRequest loanRequest,
                                  @Context LoanApplication loanApplication,
                                  @Context String loanContractNo,
                                  @Context LocalDate maturityDate,
                                  @Context LoanType loanType);


    @Mapping(target = "loanTypeName", source = "loanType.loanTypeName")
    @Mapping(target = "loanApplicationId", source = "loanApplication.loanApplicationId")
    public abstract LoanResponse toResponse(Loan loan);

}

