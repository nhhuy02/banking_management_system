package com.ctv_it.klb.util.mock;

import com.ctv_it.klb.dto.fetch.response.data.FetchCustomerDataDTO;
import org.springframework.stereotype.Component;

@Component
public class MockFetchCustomerService {

  public FetchCustomerDataDTO findById(Long customerId) {
    return FetchCustomerDataDTO.builder().build();
  }
}
