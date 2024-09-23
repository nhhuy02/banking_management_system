package com.ctv_it.klb.util.mock;

import com.ctv_it.klb.dto.baseInfo.AccountInfoDTO;
import com.ctv_it.klb.dto.baseInfo.LoanInfoDTO;
import com.ctv_it.klb.dto.baseInfo.TransactionInfoDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchAccountDataDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchCustomerDataDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchLoanDataDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchTransactionDataDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockDataService {

  private static List<FetchCustomerDataDTO> fetchCustomerDatumDTOS;
  private static List<FetchAccountDataDTO> fetchAccountDatumDTOS;
  private static List<FetchLoanDataDTO> fetchLoanDatumDTOS;
  private static List<FetchTransactionDataDTO> fetchTransactionDatumDTOS;
  private static final Random RANDOM = new Random();

  private static MockDataService instance;

  public static MockDataService initInstance() {
    if (instance == null) {
      instance = new MockDataService();
      fetchCustomerDatumDTOS = initCustomerResponses();
      fetchAccountDatumDTOS = initAccountResponses();
      fetchLoanDatumDTOS = initLoanResponses();
      fetchTransactionDatumDTOS = initTransactionResponses();
    }
    return instance;
  }

  public void getAll() {
    log.info("CustomerResponseDTOs: {}", getCustomerResponseDTO());
    log.info("AccountResponseDTOs: {}", getAccountResponseDTO());
    log.info("LoanResponseDTOs: {}", getLoanResponseDTO());
    log.info("TransactionResponseDTOs: {}", getTransactionResponseDTO());
  }

  public List<FetchCustomerDataDTO> getCustomerResponseDTO() {
    return fetchCustomerDatumDTOS;
  }

  public List<FetchAccountDataDTO> getAccountResponseDTO() {
    return fetchAccountDatumDTOS;
  }

  public List<FetchLoanDataDTO> getLoanResponseDTO() {
    return fetchLoanDatumDTOS;
  }

  public List<FetchTransactionDataDTO> getTransactionResponseDTO() {
    return fetchTransactionDatumDTOS;
  }

  private static List<FetchCustomerDataDTO> initCustomerResponses() {
    return new ArrayList<>();
  }

  private static List<FetchAccountDataDTO> initAccountResponses() {
    return new ArrayList<>();
  }

  private static List<FetchLoanDataDTO> initLoanResponses() {
    return new ArrayList<>();
  }

  private static List<FetchTransactionDataDTO> initTransactionResponses() {
    return new ArrayList<>();
  }
}
