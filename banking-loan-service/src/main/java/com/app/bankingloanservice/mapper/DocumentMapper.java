package com.app.bankingloanservice.mapper;

import com.app.bankingloanservice.dto.DocumentResponseDto;
import com.app.bankingloanservice.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    @Mapping(source = "loanApplication.loanApplicationId", target = "loanApplicationId")
    DocumentResponseDto toResponseDto(Document document);

}
