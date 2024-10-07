package com.ctv_it.klb.feignClient;

import com.ctv_it.klb.config.feignClient.FeignClientConfiguration;
import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchLoanDataResponseDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "banking-loan-service", url = "http://localhost:8060/api/v1/loan-service/loans", configuration = FeignClientConfiguration.class)
public interface LoanServiceFC {

  @GetMapping("/filters")
  FetchResponseDTO<List<FetchLoanDataResponseDTO>> filters(
      @RequestParam long accountId,
      @RequestParam(required = false) Long loanTypeId,
      @RequestParam(required = false) LocalDate loanRepaymentScheduleFrom,
      @RequestParam(required = false) LocalDate loanRepaymentScheduleTo,
      @RequestParam(required = false) Set<String> loanStatus);
}
