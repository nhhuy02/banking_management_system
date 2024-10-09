package com.ctv_it.klb.service.fetch;

import com.ctv_it.klb.dto.baseInfo.TransactionInfoDTO;
import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.transaction.FetchTransactionDataResponseDTO;
import com.ctv_it.klb.feignClient.TransactionServiceFC;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FetchTransactionServiceFC {

  private final TransactionServiceFC transactionServiceFC;
  private final HandleFetchResponse<List<FetchTransactionDataResponseDTO>> handleFetchResponseList;
  private final HandleFetchResponse<FetchTransactionDataResponseDTO> handleFetchResponse;

  public List<TransactionInfoDTO> searchMapped(String accountNumber,
      String transactionType,
      String fromDate,
      String toDate,
      String status) {

    return map(search(accountNumber, transactionType, fromDate, toDate, status));
  }

  public List<FetchTransactionDataResponseDTO> search(
      String accountNumber,
      String transactionType,
      String fromDate,
      String toDate,
      String status) {

    try {
      log.info(
          "Fetch transaction(accountNumber={}, transactionType={}, fromDate={}, toDate={}, status={}) is processing",
          accountNumber, transactionType, fromDate, toDate, status);

      FetchResponseDTO<List<FetchTransactionDataResponseDTO>> fetchDataResponseDTO = transactionServiceFC.search(
          accountNumber, transactionType, fromDate, toDate, status);

      log.info(
          "Fetch transaction(accountNumber={}, transactionType={}, fromDate={}, toDate={}, status={}) completed successfully: \n{}",
          accountNumber, transactionType, fromDate, toDate, status, fetchDataResponseDTO);

      return handleFetchResponseList.handle(fetchDataResponseDTO);
    } catch (Exception ex) {
      log.error(
          "Fetch transaction(accountNumber={}, transactionType={}, fromDate={}, toDate={}, status={}) failed: \n{}",
          accountNumber, transactionType, fromDate, toDate, status, ex.toString());

      throw ex;
    }
  }

  public FetchTransactionDataResponseDTO findLastTransactionByAccountNumberBeforeDate(
      String accountNumber, LocalDate dateBefore) {

    try {
      log.info(
          "Fetch transaction(accountNumber={}, dateBefore={}) is processing",
          accountNumber, dateBefore);

      FetchResponseDTO<FetchTransactionDataResponseDTO> fetchDataResponseDTO = transactionServiceFC.findLastTransactionByAccountNumberBeforeDate(
          accountNumber, dateBefore);

      log.info(
          "Fetch transaction(accountNumber={}, dateBefore={}) completed successfully",
          accountNumber, dateBefore);

      return handleFetchResponse.handle(fetchDataResponseDTO);
    } catch (Exception ex) {
      log.error(
          "Fetch transaction(accountNumber={}, dateBefore={}) failed: \n{}",
          accountNumber, dateBefore, ex.toString());

      throw ex;
    }
  }

  public List<TransactionInfoDTO> map(List<FetchTransactionDataResponseDTO> fetchDataResponses) {
    return fetchDataResponses.stream().map(this::map).toList();
  }


  public TransactionInfoDTO map(FetchTransactionDataResponseDTO fetchDataResponseDTO) {
    if (fetchDataResponseDTO == null) {
      return null;
    }
    return TransactionInfoDTO.builder()
        .id(fetchDataResponseDTO.getId())
        .referenceNumber(fetchDataResponseDTO.getReferenceNumber())
        .accountNumber(fetchDataResponseDTO.getAccountNumber())
        .transactionDate(fetchDataResponseDTO.getTransactionDate())
        .transactionType(fetchDataResponseDTO.getTransactionType())
        .balanceBeforeTransaction(fetchDataResponseDTO.getBalanceBeforeTransaction())
        .amount(fetchDataResponseDTO.getAmount())
        .balanceAfterTransaction(fetchDataResponseDTO.getBalanceAfterTransaction())
        .fee(fetchDataResponseDTO.getFee())
        .description(fetchDataResponseDTO.getDescription())
        .build();
  }
}
