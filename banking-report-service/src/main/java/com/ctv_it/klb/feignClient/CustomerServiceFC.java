package com.ctv_it.klb.feignClient;

import com.ctv_it.klb.config.feignClient.FeignClientConfiguration;
import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchCustomerDataResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "banking-customer-service", url = "http://localhost:8082/api/v1/customer", configuration = FeignClientConfiguration.class)
public interface CustomerServiceFC {

  @GetMapping("/{accountId}")
  FetchResponseDTO<FetchCustomerDataResponseDTO> findByAccountId(@PathVariable Long accountId);
}
