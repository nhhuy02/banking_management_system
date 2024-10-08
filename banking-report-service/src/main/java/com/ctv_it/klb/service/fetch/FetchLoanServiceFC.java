package com.ctv_it.klb.service.fetch;

import com.ctv_it.klb.dto.baseInfo.LoanInfoDTO;
import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.loan.FetchLoanDataResponseDTO;
import com.ctv_it.klb.feignClient.LoanServiceFC;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FetchLoanServiceFC {

  private final LoanServiceFC loanServiceFC;
  private final HandleFetchResponse<List<FetchLoanDataResponseDTO>> handleFetchResponse;


  public List<FetchLoanDataResponseDTO> filters(long accountId, Long loanTypeId,
      LocalDate loanRepaymentScheduleFrom, LocalDate loanRepaymentScheduleTo,
      Set<String> loanStatus) {

    try {
      log.info(
          "Fetch loan(accountId={}, loanTypeId={}, loanRepaymentScheduleFrom={}, loanRepaymentScheduleTo={}, loanStatus={}) is processing",
          accountId, loanTypeId, loanRepaymentScheduleFrom, loanRepaymentScheduleTo, loanStatus);

      FetchResponseDTO<List<FetchLoanDataResponseDTO>> fetchResponseDTO = loanServiceFC.filters(
          accountId, loanTypeId, loanRepaymentScheduleFrom, loanRepaymentScheduleTo, loanStatus);

      log.info(
          "Fetch loan(accountId={}, loanTypeId={}, loanRepaymentScheduleFrom={}, loanRepaymentScheduleTo={}, loanStatus={}) completed successfully: \n{}",
          accountId, loanTypeId, loanRepaymentScheduleFrom, loanRepaymentScheduleTo, loanStatus,
          fetchResponseDTO);

      return handleFetchResponse.handle(fetchResponseDTO);
    } catch (Exception ex) {
      log.error(
          "Fetch loan(accountId={}, loanTypeId={}, loanRepaymentScheduleFrom={}, loanRepaymentScheduleTo={}, loanStatus={}) failed: \n{}",
          accountId, loanTypeId, loanRepaymentScheduleFrom, loanRepaymentScheduleTo, loanStatus,
          ex.toString());

      throw ex;
    }
  }

  public List<LoanInfoDTO> fetchLoanByAccountId(long accountId, Long loanTypeId,
      LocalDate loanRepaymentScheduleFrom, LocalDate loanRepaymentScheduleTo,
      Set<String> loanStatus) {

    return map(filters(accountId, loanTypeId, loanRepaymentScheduleFrom, loanRepaymentScheduleTo,
        loanStatus));
  }

  public List<LoanInfoDTO> map(List<FetchLoanDataResponseDTO> fetchDataResponses) {
    return fetchDataResponses.stream().map(this::map).toList();
  }

  public LoanInfoDTO map(FetchLoanDataResponseDTO fetchDataResponse) {
    return LoanInfoDTO.builder().id(fetchDataResponse.getLoanId())
        .loanType(fetchDataResponse.getLoanTypeName()).amount(fetchDataResponse.getLoanAmount())
        .interestRate(fetchDataResponse.getCurrentInterestRate().getAnnualInterestRate())
        .loanTerm(fetchDataResponse.getLoanTermMonths())
        .disbursementDate(fetchDataResponse.getDisbursementDate())
        .maturityDate(fetchDataResponse.getMaturityDate())
        .settlementDate(fetchDataResponse.getSettlementDate())
        .repaymentSchedule(fetchDataResponse.getDisbursementDate())
        .repaymentMethod(fetchDataResponse.getRepaymentMethod())
        .amountPaid(fetchDataResponse.getTotalPaidAmount())
        .remainingBalance(fetchDataResponse.getRemainingBalance())
        .latePaymentPenalty(fetchDataResponse.getCurrentInterestRate().getPrepaymentPenaltyRate())
        .isBadDebt(fetchDataResponse.isBadDebt()).badDebtDate(fetchDataResponse.getBadDebtDate())
        .badDebtReason(fetchDataResponse.getBadDebtReason())
        .debtClassification(fetchDataResponse.getDebtClassification())
        .status(fetchDataResponse.getStatus()).build();
  }
}
