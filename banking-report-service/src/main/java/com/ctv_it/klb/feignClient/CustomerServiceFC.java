package com.ctv_it.klb.feignClient;

import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchCustomerDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "banking-customer-service", url = "http://localhost:8081/api/v1/customer")
public interface CustomerServiceFC {

  @GetMapping("/byId/{Id}")
  FetchResponseDTO<FetchCustomerDataDTO> findById(@PathVariable Long accountId);
}
