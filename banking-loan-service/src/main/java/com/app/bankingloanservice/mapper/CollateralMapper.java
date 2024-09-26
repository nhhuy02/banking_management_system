package com.app.bankingloanservice.mapper;

import com.app.bankingloanservice.dto.CollateralDto;
import com.app.bankingloanservice.entity.Collateral;
import com.app.bankingloanservice.entity.LoanApplication;
import com.app.bankingloanservice.service.LoanApplicationService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CollateralMapper {

//    @Autowired
//    private LoanApplicationService loanApplicationService;

    //@Mapping(target = "loanApplication", source = "loanApplicationId", qualifiedByName = "getLoanApplicationById")
    public abstract Collateral toEntity(CollateralDto dto);

    public abstract CollateralDto toDto(Collateral entity);

//    @Named("getLoanApplicationById")
//    public LoanApplication getLoanApplicationById(Long loanApplicationId) {
//        return loanApplicationService.getEntityById(loanApplicationId);
//    }

}
