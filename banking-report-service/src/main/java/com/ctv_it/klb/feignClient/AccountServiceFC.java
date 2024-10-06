package com.ctv_it.klb.feignClient;

import com.ctv_it.klb.config.feignClient.FeignClientConfiguration;
import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchAccountDataResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "banking-account-service", url = "http://localhost:8080/api/v1/account", configuration = FeignClientConfiguration.class)
public interface AccountServiceFC {

  @GetMapping("/{accountId}")
  FetchResponseDTO<FetchAccountDataResponseDTO> getAccountById(@PathVariable long accountId);

  @GetMapping("/savings-accounts/information/{accountId}")
  FetchResponseDTO<FetchAccountDataResponseDTO> getSavingsAccountById(@PathVariable long savingAccountId);
}
