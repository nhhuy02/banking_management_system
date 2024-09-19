package com.ctv_it.klb.util.FakeMockServices;

import com.ctv_it.klb.dto.base.AccountInfoDTO;
import com.ctv_it.klb.dto.base.LoanInfoDTO;
import com.ctv_it.klb.dto.base.TransactionInfoDTO;
import com.ctv_it.klb.dto.fetch.response.FetchAccountDataDTO;
import com.ctv_it.klb.dto.fetch.response.FetchCustomerDataDTO;
import com.ctv_it.klb.dto.fetch.response.FetchLoanDataDTO;
import com.ctv_it.klb.dto.fetch.response.FetchTransactionDataDTO;
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
public class FakeDataGenerator {

  private static List<FetchCustomerDataDTO> fetchCustomerDatumDTOS;
  private static List<FetchAccountDataDTO> fetchAccountDatumDTOS;
  private static List<FetchLoanDataDTO> fetchLoanDatumDTOS;
  private static List<FetchTransactionDataDTO> fetchTransactionDatumDTOS;
  private static final Random RANDOM = new Random();

  private static FakeDataGenerator instance;

  public static FakeDataGenerator initInstance() {
    if (instance == null) {
      instance = new FakeDataGenerator();
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
    return IntStream.range(1, 6).mapToObj(
        i -> FetchCustomerDataDTO.builder()
            .id((long) i)
            .name("Customer " + i)
            .phoneNumber("09012345" + (i + 1))
            .address("Address " + i)
            .nationalId(String.format("%09d", RANDOM.nextInt(1000000000)))
            .email("customer" + i + "@example.com")
            .build()
    ).collect(Collectors.toList());
  }

  private static List<FetchAccountDataDTO> initAccountResponses() {
    List<AccountInfoDTO> accounts = IntStream.range(1, 11).mapToObj(
        i -> AccountInfoDTO.builder()
            .id((long) i)
            .type(RANDOM.nextBoolean() ? "Saving" : "Checking")
            .number(String.format("%10d", RANDOM.nextInt(1000000000)))
            .currency("VND")
            .accountingBalance(new BigDecimal(RANDOM.nextInt(10000000)))
            .availableBalance(new BigDecimal(RANDOM.nextInt(10000000)))
            .branch("Branch " + (RANDOM.nextInt(3) + 1))
            .status("Active")
            .openingDate(LocalDate.now().minusDays(RANDOM.nextInt(365 * 5)))
            .build()
    ).collect(Collectors.toList());

    List<FetchAccountDataDTO> responses = new ArrayList<>();
    for (long customerId = 1; customerId <= 5; customerId++) {
      long finalCustomerId = customerId;
      FetchAccountDataDTO response = FetchAccountDataDTO.builder()
          .customerId(customerId)
          .accounts(accounts.stream()
              .filter(account -> (account.getId() % 5) == (finalCustomerId % 5))
              .collect(Collectors.toList()))
          .build();
      responses.add(response);
    }

    return responses;
  }

  private static List<FetchLoanDataDTO> initLoanResponses() {
    List<LoanInfoDTO> loans = IntStream.range(1, 11).mapToObj(
        i -> LoanInfoDTO.builder()
            .id((long) i)
            .loanType(RANDOM.nextBoolean() ? "Home" : "Car")
            .amount(new BigDecimal(RANDOM.nextInt(10000000)))
            .interestRate(new BigDecimal(RANDOM.nextInt(10) + 1))
            .loanTerm(RANDOM.nextInt(30) + 1)
            .startDate(LocalDate.now().minusMonths(RANDOM.nextInt(120)))
            .endDate(LocalDate.now().plusMonths(RANDOM.nextInt(120)))
            .maturityDate(LocalDate.now().plusMonths(RANDOM.nextInt(120)))
            .repaymentSchedule(LocalDate.now().plusMonths(RANDOM.nextInt(12)))
            .loanTermPaid(RANDOM.nextInt(12))
            .amountPaid(new BigDecimal(RANDOM.nextInt(1000000)))
            .status("Active")
            .latePaymentPenalty(new BigDecimal(RANDOM.nextInt(5000)))
            .build()
    ).toList();

    List<FetchLoanDataDTO> responses = new ArrayList<>();
    for (long customerId = 1; customerId <= 5; customerId++) {
      long finalCustomerId = customerId;
      FetchLoanDataDTO response = FetchLoanDataDTO.builder()
          .customerId(customerId)
          .loans(loans.stream()
              .filter(loan -> (loan.getId() % 5) == (finalCustomerId % 5))
              .collect(Collectors.toList()))
          .build();
      responses.add(response);
    }

    return responses;
  }

  private static List<FetchTransactionDataDTO> initTransactionResponses() {
    List<TransactionInfoDTO> transactions = IntStream.range(1, 11).mapToObj(
        i -> TransactionInfoDTO.builder()
            .id((long) i)
            .sourceAccountId((long) (RANDOM.nextInt(10) + 1))
            .destinationAccountId((long) (RANDOM.nextInt(10) + 1))
            .date(LocalDateTime.now().minusDays(RANDOM.nextInt(30)))
            .amount(new BigDecimal(RANDOM.nextInt(1000000)))
            .balanceBeforeTransaction(new BigDecimal(RANDOM.nextInt(10000000)))
            .balanceAfterTransaction(new BigDecimal(RANDOM.nextInt(10000000)))
            .type(RANDOM.nextBoolean() ? "withdraw" : "deposit")
            .category(RANDOM.nextBoolean() ? "shopping" : "salary")
            .description("Transaction description " + i)
            .status("completed")
            .updatedAt(LocalDateTime.now())
            .note("Note " + i)
            .fee(new BigDecimal(RANDOM.nextInt(100)))
            .transactionChanel(RANDOM.nextBoolean() ? "online" : "branch")
            .build()
    ).toList();

    List<FetchTransactionDataDTO> responses = new ArrayList<>();
    for (long customerId = 1; customerId <= 5; customerId++) {
      long finalCustomerId = customerId;
      FetchTransactionDataDTO response = FetchTransactionDataDTO.builder()
          .customerId(customerId)
          .transactions(transactions.stream()
              .filter(transaction -> transaction.getSourceAccountId() % 5 == finalCustomerId % 5)
              .collect(Collectors.toList()))
          .build();
      responses.add(response);
    }

    return responses;
  }
}
