package com.ctv_it.klb.feignClient;

import com.ctv_it.klb.config.feignClient.FeignClientConfiguration;
import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchAccountDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "banking-account-service", url = "http://localhost:8080/api/v1/account", configuration = FeignClientConfiguration.class)
public interface AccountServiceFC {

  @GetMapping("/{id}")
  FetchResponseDTO<FetchAccountDataDTO> getAccountById(@PathVariable long id);

  @GetMapping("/savings-accounts/information/{id}")
  FetchResponseDTO<FetchAccountDataDTO> getSavingsAccountById(@PathVariable long id);
}
