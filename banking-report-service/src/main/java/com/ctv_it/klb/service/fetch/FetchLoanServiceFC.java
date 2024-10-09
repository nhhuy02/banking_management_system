package com.ctv_it.klb.service.fetch;

import com.ctv_it.klb.dto.baseInfo.LoanInfoDTO;
import com.ctv_it.klb.dto.baseInfo.LoanInfoDTO.LoanRepaymentInfoDTO;
import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.loan.FetchLoanDataResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.loan.FetchLoanRepaymentDataResponseDTO;
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
  private final HandleFetchResponse<FetchLoanDataResponseDTO> handleFetchLoanResponse;
  private final HandleFetchResponse<List<FetchLoanDataResponseDTO>> handleFetchLoansResponse;
  private final HandleFetchResponse<List<FetchLoanRepaymentDataResponseDTO>> handleFetchLoanRepaymentsResponse;

  public FetchLoanDataResponseDTO FetLoanById(long id) {
    try {
      log.info("Fetch loan(id={}) is processing", id);

      FetchResponseDTO<FetchLoanDataResponseDTO> fetchResponseDTO = loanServiceFC.findById(id);

      log.info("Fetch loan(id={}) completed successfully: \n{}", id, fetchResponseDTO);

      return handleFetchLoanResponse.handle(fetchResponseDTO);
    } catch (Exception ex) {
      log.error("Fetch loan(id={}) failed: \n{}", id, ex.toString());
      throw ex;
    }
  }

  public LoanInfoDTO FetLoanByIdMapped(long id) {
    return mapLoan(FetLoanById(id));
  }


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

      return handleFetchLoansResponse.handle(fetchResponseDTO);
    } catch (Exception ex) {
      log.error(
          "Fetch loan(accountId={}, loanTypeId={}, loanRepaymentScheduleFrom={}, loanRepaymentScheduleTo={}, loanStatus={}) failed: \n{}",
          accountId, loanTypeId, loanRepaymentScheduleFrom, loanRepaymentScheduleTo, loanStatus,
          ex.toString());

      throw ex;
    }
  }

  public List<FetchLoanRepaymentDataResponseDTO> fetchLoanRepaymentByLoanId(long loanId) {
    try {
      log.info("Fetch repayments(loanId={}) is processing", loanId);

      FetchResponseDTO<List<FetchLoanRepaymentDataResponseDTO>> fetchResponseDTO = loanServiceFC.findAlLRepaymentsById(
          loanId);

      log.info("Fetch repayments(loanId={}) completed successfully: \n{}", loanId,
          fetchResponseDTO);

      return handleFetchLoanRepaymentsResponse.handle(fetchResponseDTO);
    } catch (Exception ex) {
      log.error("Fetch repayments(loanId={}) failed: \n{}", loanId, ex.toString());
      throw ex;
    }
  }

  public List<LoanInfoDTO> fetchLoanByAccountIdMapped(long accountId, Long loanTypeId,
      LocalDate loanRepaymentScheduleFrom, LocalDate loanRepaymentScheduleTo,
      Set<String> loanStatus) {

    return mapLoans(
        filters(accountId, loanTypeId, loanRepaymentScheduleFrom, loanRepaymentScheduleTo,
            loanStatus));
  }

  public List<LoanRepaymentInfoDTO> fetchLoanRepaymentByLoanIdMapped(long loanId) {
    return mapLoanRepayments(fetchLoanRepaymentByLoanId(loanId));
  }


  public List<LoanInfoDTO> mapLoans(List<FetchLoanDataResponseDTO> fetchDataResponses) {
    return fetchDataResponses.stream().map(this::mapLoan).toList();
  }

  public LoanInfoDTO mapLoan(FetchLoanDataResponseDTO fetchDataResponse) {
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

  public List<LoanRepaymentInfoDTO> mapLoanRepayments(
      List<FetchLoanRepaymentDataResponseDTO> fetchDataResponses) {
    return fetchDataResponses.stream().map(this::mapLoanRepayment).toList();
  }

  public LoanRepaymentInfoDTO mapLoanRepayment(
      FetchLoanRepaymentDataResponseDTO fetchDataResponse) {
    return LoanRepaymentInfoDTO.builder()
        .loanPaymentId(fetchDataResponse.getLoanPaymentId())
        .principalAmount(fetchDataResponse.getPrincipalAmount())
        .interestAmount(fetchDataResponse.getInterestAmount())
        .latePaymentInterestAmount(fetchDataResponse.getLatePaymentInterestAmount())
        .totalAmount(fetchDataResponse.getTotalAmount())
        .paymentDueDate(fetchDataResponse.getPaymentDueDate())
        .actualPaymentDate(fetchDataResponse.getActualPaymentDate())
        .transactionReference(fetchDataResponse.getTransactionReference())
        .isLate(fetchDataResponse.isLate())
        .paymentStatus(fetchDataResponse.getPaymentStatus())
        .build();
  }
}
