package com.app.bankingloanservice.mapper;

import com.app.bankingloanservice.dto.DocumentResponse;
import com.app.bankingloanservice.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    @Mapping(source = "loanApplication.loanApplicationId", target = "loanApplicationId")
    DocumentResponse toResponse(Document document);

}
