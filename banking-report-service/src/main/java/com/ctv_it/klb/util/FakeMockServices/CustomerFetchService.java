package com.ctv_it.klb.util.FakeMockServices;

import com.ctv_it.klb.config.exception.NotFoundExceptionCustomize;
import com.ctv_it.klb.dto.fetch.response.FetchCustomerDataDTO;
import com.ctv_it.klb.dto.response.ErrorDetailDTO;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class CustomerFetchService {

  public FetchCustomerDataDTO findById(Long customerId) {
    return FakeDataGenerator.initInstance().getCustomerResponseDTO().stream()
        .filter(cus -> Objects.equals(cus.getId(), customerId))
        .findFirst().orElseThrow(() -> new NotFoundExceptionCustomize(List.of(
            ErrorDetailDTO.builder().field("customerId").rejectedValue(customerId)
                .message("{not-found}").build())));
  }
}
