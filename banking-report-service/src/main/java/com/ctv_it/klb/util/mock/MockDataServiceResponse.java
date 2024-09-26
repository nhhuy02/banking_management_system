package com.ctv_it.klb.util.mock;

import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchAccountDataDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchCustomerDataDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchLoanDataDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockDataServiceResponse {

  FetchResponseDTO<List<FetchAccountDataDTO>> mockAccounts;
  FetchResponseDTO<List<FetchCustomerDataDTO>> mockCustomers;
  FetchResponseDTO<List<FetchLoanDataDTO>> mockLoans;

  public MockDataServiceResponse() {
    mockAccounts = getMockAccounts();
    log.info("Init mockAccounts: {}", mockAccounts);

    mockCustomers = getMockCustomers();
    log.info("Init mockCustomers: {}", mockCustomers);

    mockLoans = getMockLoans();
    log.info("Init mockLoans: {}", mockLoans);
  }


  public FetchResponseDTO<List<FetchAccountDataDTO>> mockAccounts() {
    return mockAccounts;
  }

  public FetchResponseDTO<List<FetchCustomerDataDTO>> mockCustomers() {
    return mockCustomers;
  }

  public FetchResponseDTO<List<FetchLoanDataDTO>> mockLoans() {
    return mockLoans;
  }

  private static final Random random = new Random();

  private FetchResponseDTO<List<FetchAccountDataDTO>> getMockAccounts() {
    List<FetchAccountDataDTO> accounts = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      FetchAccountDataDTO account = FetchAccountDataDTO.builder()
          .id((long) i + 1)
          .accountName("Account " + (i + 1))
          .accountNumber(1000000000L + i)
          .balance((long) (random.nextDouble() * 10000))
          .status("ACTIVE")
          .openingDate(LocalDateTime.now().minusDays(random.nextInt(30)))
          .build();
      accounts.add(account);
    }

    return FetchResponseDTO.<List<FetchAccountDataDTO>>builder()
        .success(true)
        .status(200)
        .message("Fetched accounts successfully.")
        .data(accounts)
        .build();
  }

  private FetchResponseDTO<List<FetchCustomerDataDTO>> getMockCustomers() {
    List<FetchCustomerDataDTO> customers = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      FetchCustomerDataDTO customer = FetchCustomerDataDTO.builder()
          .id((long) i + 1)
          .accountId((long) (random.nextInt(10) + 1)) // Random account ID from 1 to 10
          .fullName("Customer " + (i + 1))
          .dateOfBirth(LocalDate.now().minusYears(random.nextInt(50) + 18)) // Age between 18 and 67
          .gender(random.nextBoolean() ? "Male" : "Female")
          .email("customer" + (i + 1) + "@example.com")
          .phoneNumber("012345678" + i)
          .currentAddress("Address " + (i + 1))
          .kyc(FetchCustomerDataDTO.Kyc.builder()
              .documentType("ID Card")
              .documentNumber("ID" + (i + 1))
              .build())
          .kycStatus("VERIFIED")
          .build();
      customers.add(customer);
    }

    return FetchResponseDTO.<List<FetchCustomerDataDTO>>builder()
        .success(true)
        .status(200)
        .message("Fetched customers successfully.")
        .data(customers)
        .build();
  }

  private FetchResponseDTO<List<FetchLoanDataDTO>> getMockLoans() {
    List<FetchLoanDataDTO> loans = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      FetchLoanDataDTO loan = FetchLoanDataDTO.builder()
          .loanId(i + 1)
          .customerId((int) (random.nextInt(10) + 1)) // Random customer ID from 1 to 10
          .loanContractNo("LN" + (i + 1))
          .loanType(FetchLoanDataDTO.LoanType.builder()
              .loanTypeName("Personal Loan")
              .build())
          .loanAmount(random.nextDouble() * 50000 + 5000) // Loan amount between 5,000 and 50,000
          .currentInterestRate(FetchLoanDataDTO.CurrentInterestRate.builder()
              .rate(random.nextDouble() * 10 + 1) // Interest rate between 1% and 10%
              .build())
          .loanTermMonths(random.nextInt(60) + 12) // Loan term between 12 and 60 months
          .disbursementDate(LocalDate.now().minusDays(random.nextInt(365)))
          .maturityDate(LocalDate.now().plusMonths(random.nextInt(12)))
          .settlementDate(LocalDate.now().plusMonths(random.nextInt(12)))
          .renewalCount(random.nextInt(3)) // 0 to 2 renewals
          .remainingBalance(random.nextDouble() * 50000)
          .totalPaidAmount(random.nextDouble() * 50000)
          .isBadDebt(random.nextBoolean())
          .badDebtDate(random.nextBoolean() ? LocalDate.now().minusDays(random.nextInt(100)) : null)
          .badDebtReason(random.nextBoolean() ? "Missed Payments" : null)
          .debtClassification("Normal")
          .collateral(FetchLoanDataDTO.Collateral.builder()
              .collateralType("Property")
              .collateralValue(random.nextDouble() * 100000)
              .build())
          .loanRepayments(new ArrayList<>()) // Assuming no repayments for now
          .loanSettlement(FetchLoanDataDTO.LoanSettlement.builder()
              .loanSettlementId(i + 1)
              .settlementDate(LocalDate.now().minusDays(random.nextInt(30)))
              .settlementStatus("Settled")
              .build())
          .status("ACTIVE")
          .build();
      loans.add(loan);
    }

    return FetchResponseDTO.<List<FetchLoanDataDTO>>builder()
        .success(true)
        .status(200)
        .message("Fetched loans successfully.")
        .data(loans)
        .build();
  }
}
