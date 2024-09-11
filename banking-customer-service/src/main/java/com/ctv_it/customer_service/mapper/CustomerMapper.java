package com.ctv_it.customer_service.mapper;

import com.ctv_it.customer_service.dto.CustomerDto;
import com.ctv_it.customer_service.dto.CustomerUpdateDto;
import com.ctv_it.customer_service.model.Customer;
import com.ctv_it.customer_service.model.Kyc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping( source = "kyc.id", target = "kyc")
    @Mapping(source = "kyc.documentNumber", target = "kyc.documentNumber")
    CustomerDto toDto(Customer customer);

    @Mapping( source = "kyc", target = "kyc.id")
    Customer toEntity(CustomerDto customerDto);

    CustomerUpdateDto toUpdateDto(Customer customer);

    @Mapping(target = "accountId", ignore = true)
    void updateCustomerFromDto(CustomerUpdateDto dto, @MappingTarget Customer customer);

    //Long to Kyc
    default Kyc map(Long id) {
        if (id == null) {
            return null;
        }
        Kyc kyc = new Kyc();
        kyc.setId(id);
        return kyc;
    }

    // kyc to longId
    default Long map(Kyc kyc) {
        return kyc != null ? kyc.getId() : null;
    }

}
