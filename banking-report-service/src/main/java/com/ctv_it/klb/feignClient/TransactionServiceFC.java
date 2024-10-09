package com.ctv_it.klb.feignClient;

import com.ctv_it.klb.config.feignClient.FeignClientConfiguration;
import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.transaction.FetchTransactionDataResponseDTO;
import java.time.LocalDate;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "banking-transaction-service", url = "http://localhost:8070/api/v1/transactions", configuration = FeignClientConfiguration.class)
public interface TransactionServiceFC {

  @GetMapping("/search")
  FetchResponseDTO<List<FetchTransactionDataResponseDTO>> search(
      @RequestParam String accountNumber,
      @RequestParam(required = false) String transactionType,
      @RequestParam(required = false) String fromDate,
      @RequestParam(required = false) String toDate,
      @RequestParam(required = false) String status);

  @GetMapping("/last")
  FetchResponseDTO<FetchTransactionDataResponseDTO> findLastTransactionByAccountNumberBeforeDate(
      @RequestParam String accountNumber, @RequestParam LocalDate dateBefore);
}
