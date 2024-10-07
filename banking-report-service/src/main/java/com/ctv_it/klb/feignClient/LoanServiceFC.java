package com.ctv_it.klb.feignClient;

import com.ctv_it.klb.config.feignClient.FeignClientConfiguration;
import com.ctv_it.klb.dto.baseInfo.TransactionInfoDTO;
import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchLoanDataResponseDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "banking-loan-service", url = "http://localhost:8060/api/v1/loan-service/loans", configuration = FeignClientConfiguration.class)
public interface LoanServiceFC {

  @GetMapping
  FetchResponseDTO<FetchLoanDataResponseDTO> findByAccountId(@RequestParam long accountId);

// update later
//  @GetMapping("/search")
//  FetchResponseDTO<FetchLoanDataResponseDTO> search(
//      @RequestParam String loanType,
//      @RequestParam(required = false) String transactionType,
//      @RequestParam(required = false) String fromDate,
//      @RequestParam(required = false) String toDate,
//      @RequestParam(required = false) String status);
}
