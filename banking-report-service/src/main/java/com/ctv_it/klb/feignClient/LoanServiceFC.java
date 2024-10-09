package com.ctv_it.klb.feignClient;

import com.ctv_it.klb.config.feignClient.FeignClientConfiguration;
import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.loan.FetchLoanDataResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.loan.FetchLoanRepaymentDataResponseDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @GetMapping("/{id}")
  FetchResponseDTO<FetchLoanDataResponseDTO> findById(@PathVariable long id);

  @GetMapping("/{id}/repayments")
  FetchResponseDTO<List<FetchLoanRepaymentDataResponseDTO>> findAlLRepaymentsById(
      @PathVariable long id);
}
