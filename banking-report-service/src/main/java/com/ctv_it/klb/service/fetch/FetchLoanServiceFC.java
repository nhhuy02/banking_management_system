package com.ctv_it.klb.service.fetch;

import com.ctv_it.klb.dto.baseInfo.LoanInfoDTO;
import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchLoanDataResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchLoanDataResponseDTO.FetchLoanContentResponseDTO;
import com.ctv_it.klb.feignClient.LoanServiceFC;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FetchLoanServiceFC {

  private final LoanServiceFC loanServiceFC;
  private final HandleFetchResponse<FetchLoanDataResponseDTO> handleFetchResponse;

  public List<LoanInfoDTO> fetchLoanByAccountId(long accountId) {
    return map(findByAccountId(accountId));
  }

  public FetchLoanDataResponseDTO findByAccountId(long accountId) {
    try {
      log.info("Fetch loan(accountId={}) is processing", accountId);
      FetchResponseDTO<FetchLoanDataResponseDTO> fetchResponseDTO = loanServiceFC.findByAccountId(
          accountId);
      log.info("Fetch loan(accountId={}) passed: {}", accountId, fetchResponseDTO);

      return handleFetchResponse.handle(fetchResponseDTO);
    } catch (Exception ex) {
      log.error("Fetch loan(accountId={}) failed: {}", accountId, ex.toString());
      throw ex;
    }
  }

  public LoanInfoDTO map(FetchLoanContentResponseDTO fetchContentResponse) {
    return LoanInfoDTO.builder()
        .id(fetchContentResponse.getLoanId())
        .loanType(fetchContentResponse.getLoanTypeName())
        .amount(fetchContentResponse.getLoanAmount())
        .interestRate(
            fetchContentResponse.getCurrentInterestRate().getAnnualInterestRate())
        .loanTerm(fetchContentResponse.getLoanTermMonths())
        .disbursementDate(fetchContentResponse.getDisbursementDate())
        .maturityDate(fetchContentResponse.getMaturityDate())
        .settlementDate(fetchContentResponse.getSettlementDate())
        .repaymentSchedule(fetchContentResponse.getDisbursementDate())
        .repaymentMethod(fetchContentResponse.getRepaymentMethod())
        .amountPaid(fetchContentResponse.getTotalPaidAmount())
        .remainingBalance(fetchContentResponse.getRemainingBalance())
        .latePaymentPenalty(fetchContentResponse.getCurrentInterestRate()
            .getPrepaymentPenaltyRate())
        .isBadDebt(fetchContentResponse.isBadDebt())
        .badDebtDate(fetchContentResponse.getBadDebtDate())
        .badDebtReason(fetchContentResponse.getBadDebtReason())
        .debtClassification(fetchContentResponse.getDebtClassification())
        .status(fetchContentResponse.getStatus())
        .build();
  }

  public List<LoanInfoDTO> map(FetchLoanDataResponseDTO fetchDataResponseDTO) {
    return fetchDataResponseDTO.getContent().stream().map(this::map).collect(Collectors.toList());
  }
}
