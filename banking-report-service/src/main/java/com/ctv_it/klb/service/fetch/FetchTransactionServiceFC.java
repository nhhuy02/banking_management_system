package com.ctv_it.klb.service.fetch;

import com.ctv_it.klb.dto.fetch.response.data.FetchTransactionDataResponseDTO;
import com.ctv_it.klb.feignClient.TransactionServiceFC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FetchTransactionServiceFC {

  private final TransactionServiceFC transactionServiceFC;

  public FetchTransactionDataResponseDTO search(
      String accountNumber,
      String transactionType,
      String fromDate,
      String toDate,
      String status) {

    try {
      log.info(
          "Fetch transaction(accountNumber={}, transactionType={}, fromDate={}, toDate={}, status={}) is processing",
          accountNumber, transactionType, fromDate, toDate, status);

      FetchTransactionDataResponseDTO fetchDataResponseDTO = FetchTransactionDataResponseDTO.builder()
          .transactions(
              transactionServiceFC.search(accountNumber, transactionType, fromDate, toDate, status))
          .build();

      log.info(
          "Fetched transaction(accountNumber={}, transactionType={}, fromDate={}, toDate={}, status={}) successfully: {}",
          accountNumber, transactionType, fromDate, toDate, status, fetchDataResponseDTO);

      return fetchDataResponseDTO;
    } catch (Exception ex) {
      log.error(
          "Fetch transaction(accountNumber={}, transactionType={}, fromDate={}, toDate={}, status={}) fail: {}",
          accountNumber, transactionType, fromDate, toDate, status, ex.toString());

      throw ex;
    }
  }
}
