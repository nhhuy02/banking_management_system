package com.ctv_it.customer_service.mapper;

import com.ctv_it.customer_service.dto.CustomersStatusHistoryDto;
import com.ctv_it.customer_service.model.CustomersStatusHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomersStatusHistoryMapper {

    CustomersStatusHistoryMapper INSTANCE = Mappers.getMapper(CustomersStatusHistoryMapper.class);

    @Mapping(source = "customer.id", target = "customerId")
    CustomersStatusHistoryDto toDto(CustomersStatusHistory customersStatusHistory);

    @Mapping(source = "customerId", target = "customer.id")
    CustomersStatusHistory toEntity(CustomersStatusHistoryDto customersStatusHistoryDto);
}
