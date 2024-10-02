package com.ctv_it.klb.feignClient;

import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchAccountDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "banking-account-service", url = "http://localhost:8040/api/v1")
public interface AccountServiceFC {

  @GetMapping("/account/{id}")
  FetchResponseDTO<FetchAccountDataDTO> getAccountById(@PathVariable long id);

  @GetMapping("/savings-accounts/information/{id}")
  FetchResponseDTO<FetchAccountDataDTO> getSavingsAccountById(@PathVariable long id);
}