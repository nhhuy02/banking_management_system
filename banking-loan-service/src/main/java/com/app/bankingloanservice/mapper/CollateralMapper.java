package com.app.bankingloanservice.mapper;

import com.app.bankingloanservice.dto.CollateralRequest;
import com.app.bankingloanservice.dto.CollateralResponse;
import com.app.bankingloanservice.entity.Collateral;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = DocumentMapper.class)
public abstract class CollateralMapper {

    public abstract Collateral toEntity(CollateralRequest dto);

    @Mapping(target = "loanId", source = "loan.loanId")
    @Mapping(target = "loanApplicationId", source = "loanApplication.loanApplicationId")
    @Mapping(target = "documentResponse", source = "document")
    public abstract CollateralResponse toResponse(Collateral entity);

}
