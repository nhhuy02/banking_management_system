package com.ctv_it.klb.service.type.impl;

import com.ctv_it.klb.dto.TransactionReportDTO;
import com.ctv_it.klb.dto.baseInfo.AccountInfoDTO;
import com.ctv_it.klb.dto.baseInfo.CustomerInfoDTO;
import com.ctv_it.klb.dto.baseInfo.TransactionInfoDTO;
import com.ctv_it.klb.dto.fetch.response.data.transaction.FetchTransactionDataResponseDTO;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import com.ctv_it.klb.dto.filter.extend.TransactionFilterDTO;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.fetch.FetchAccountServiceFC;
import com.ctv_it.klb.service.fetch.FetchCustomerServiceFC;
import com.ctv_it.klb.service.fetch.FetchTransactionServiceFC;
import com.ctv_it.klb.service.type.ReportTypeService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionReportServiceImpl implements ReportTypeService<TransactionReportDTO> {

  private final FetchAccountServiceFC fetchAccountServiceFC;
  private final FetchCustomerServiceFC fetchCustomerServiceFC;
  private final FetchTransactionServiceFC fetchTransactionServiceFC;

  @Override
  public ReportType getType() {
    return ReportType.TRANSACTION;
  }

  @Override
  public TransactionReportDTO search(Long accountId, ReportFilterDTO reportFilterDTO) {
    log.info("Search(type={}, accountId={}, filters={}) is processing", getType(), accountId,
        reportFilterDTO);

    TransactionFilterDTO transactionFilters = (TransactionFilterDTO) reportFilterDTO;

    CustomerInfoDTO customer = fetchCustomerServiceFC.findByAccountIdMapped(accountId);
    AccountInfoDTO account = fetchAccountServiceFC.findAccountByAccountIdMapped(accountId);
    List<TransactionInfoDTO> transactions = search(account.getNumber(), transactionFilters);

    BigDecimal openingBalance = BigDecimal.ZERO;
    BigDecimal endBalance = BigDecimal.ZERO;
    BigDecimal credit;
    BigDecimal debit;

    /*
     * If transactions exist:
     * - openingBalance is set to the balance before the first transaction.
     * - endBalance is set to the balance after the last transaction.
     */
    if (!transactions.isEmpty()) {
      openingBalance = transactions.getFirst().getBalanceBeforeTransaction();
      endBalance = transactions.getLast().getBalanceAfterTransaction();
    } else {
      /*
       * If no transactions exist:
       * - Find the last transaction that occurred before the minimum date in the filter range.
       * - If a last transaction exists before this date, set openingBalance to the balance after that transaction.
       * - If no such transaction exists, set openingBalance to 0 (default balance of the account).
       */
      LocalDate dateFilterFrom =
          transactionFilters != null && transactionFilters.getTransactionDateRange() != null
              && transactionFilters.getTransactionDateRange().getMin() != null ?
              transactionFilters.getTransactionDateRange().getMin() : LocalDate.MIN;

      FetchTransactionDataResponseDTO fetchDataResponse = fetchTransactionServiceFC.findLastTransactionByAccountNumberBeforeDate(
          account.getNumber(), dateFilterFrom);
      openingBalance = fetchDataResponse.getBalanceAfterTransaction();
    }

    BigDecimal[] creditAndDebit = transactions.stream()
        .reduce(new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO}, (cad, transaction) -> {
          if (transaction.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            cad[0] = cad[0].add(transaction.getAmount());  // Accumulate credit
          } else {
            cad[1] = cad[1].add(transaction.getAmount());  // Accumulate debit
          }
          return cad;
        }, (acc1, acc2) -> acc1);

    credit = creditAndDebit[0];
    debit = creditAndDebit[1];

    return TransactionReportDTO.builder().customer(customer).account(account)
        .openingBalance(openingBalance).credit(credit).debit(debit).endBalance(endBalance)
        .transactions(transactions).build();
  }

  private List<TransactionInfoDTO> search(String accountNumber,
      TransactionFilterDTO transactionFilterDTO) {

    String transactionType = null;
    LocalDate fromDate = null;
    LocalDate toDate = null;
    String transactionStatus = null;
    if (transactionFilterDTO != null) {
      transactionType = transactionFilterDTO.getTransactionType();
      if (transactionFilterDTO.getTransactionDateRange() != null) {
        fromDate = transactionFilterDTO.getTransactionDateRange().getMin();
        toDate = transactionFilterDTO.getTransactionDateRange().getMax();
      }
      transactionStatus = transactionFilterDTO.getTransactionStatus();
    }

    return fetchTransactionServiceFC.searchMapped(accountNumber, transactionType,
        fromDate == null ? null : fromDate.toString(), toDate == null ? null : toDate.toString(),
        transactionStatus);
  }
}
