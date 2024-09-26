package com.app.bankingloanservice.mapper;

import com.app.bankingloanservice.dto.LoanApplicationRequestDto;
import com.app.bankingloanservice.dto.LoanApplicationResponseDto;
import com.app.bankingloanservice.dto.LoanTypeDto;
import com.app.bankingloanservice.entity.LoanApplication;
import com.app.bankingloanservice.entity.LoanType;
import com.app.bankingloanservice.service.LoanTypeService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public abstract class LoanApplicationMapper {

    @Autowired
    private LoanTypeService loanTypeService;

    @Autowired
    private LoanTypeMapper loanTypeMapper;

    // Mapping từ LoanApplicationRequestDto sang LoanApplication
    @Mapping(target = "loanApplicationId", ignore = true)  // Bỏ qua loanApplicationId
    @Mapping(target = "loanType", source = "loanTypeId", qualifiedByName = "getLoanTypeById")  // Ánh xạ loanTypeId
    @Mapping(target = "collateral", ignore = true)  // Bỏ qua collateral
    @Mapping(target = "documents", ignore = true)  // Bỏ qua documents
    @Mapping(target = "loan", ignore = true)  // Bỏ qua loan
    @Mapping(target = "applicationStatus", constant = "PENDING")  // Set giá trị cố định cho applicationStatus
    @Mapping(target = "submissionDate", expression = "java(LocalDate.now())")  // Lấy ngày hiện tại cho submissionDate
    @Mapping(target = "reviewDueDate", ignore = true)  // Bỏ qua reviewDueDate
    @Mapping(target = "reviewDate", ignore = true)  // Bỏ qua reviewDate
    @Mapping(target = "reviewNotes", ignore = true)  // Bỏ qua reviewNotes
    public abstract LoanApplication toEntity(LoanApplicationRequestDto dto);

    // Phương thức get LoanType từ LoanTypeService
    @Named("getLoanTypeById")
    public LoanType mapLoanTypeById(Long loanTypeId) {
        return loanTypeService.getLoanTypeById(loanTypeId);
    }

//    // Phương thức cập nhật LoanApplication từ DTO (dùng khi update)
//    @Mapping(target = "loanApplicationId", ignore = true)  // Bỏ qua loanApplicationId
//    @Mapping(target = "loanType", source = "loanTypeId", qualifiedByName = "mapLoanTypeById")  // Ánh xạ loanTypeId
//    @Mapping(target = "collateral", ignore = true)  // Bỏ qua collateral
//    @Mapping(target = "documents", ignore = true)  // Bỏ qua documents
//    @Mapping(target = "loan", ignore = true)  // Bỏ qua loan
//    @Mapping(target = "applicationStatus", constant = "PENDING")  // Set giá trị cố định cho applicationStatus
//    @Mapping(target = "submissionDate", expression = "java(LocalDate.now())")  // Lấy ngày hiện tại cho submissionDate
//    @Mapping(target = "reviewDueDate", ignore = true)  // Bỏ qua reviewDueDate
//    @Mapping(target = "reviewDate", ignore = true)  // Bỏ qua reviewDate
//    @Mapping(target = "reviewNotes", ignore = true)  // Bỏ qua reviewNotes
//    public abstract void updateLoanApplicationFromDto(LoanApplicationRequestDto dto, @MappingTarget LoanApplication loanApplication);

    // Thêm phương thức ánh xạ LoanApplication sang LoanApplicationResponseDto
    @Mapping(target = "loanTypeDto", source = "loanType", qualifiedByName = "mapLoanTypeToDto")
    public abstract LoanApplicationResponseDto toResponseDto(LoanApplication loanApplication);

    // Phương thức ánh xạ LoanType sang LoanTypeDto (cần được định nghĩa)
    @Named("mapLoanTypeToDto")
    public LoanTypeDto mapLoanTypeToDto(LoanType loanType) {
        return loanTypeMapper.toDto(loanType);
    }
}
