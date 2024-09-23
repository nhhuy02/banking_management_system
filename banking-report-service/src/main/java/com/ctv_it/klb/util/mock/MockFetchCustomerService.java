package com.ctv_it.klb.util.mock;

import com.ctv_it.klb.config.exception.NotFoundExceptionCustomize;
import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.fetch.response.data.FetchCustomerDataDTO;
import com.ctv_it.klb.dto.response.ErrorDetailDTO;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class MockFetchCustomerService {

  public FetchCustomerDataDTO findById(Long customerId) {
    return FetchCustomerDataDTO.builder().build();
  }
}
