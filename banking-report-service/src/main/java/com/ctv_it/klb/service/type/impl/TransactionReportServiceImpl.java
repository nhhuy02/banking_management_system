package com.ctv_it.klb.service.type.impl;

import com.ctv_it.klb.dto.TransactionReportDTO;
import com.ctv_it.klb.dto.baseInfo.AccountInfoDTO;
import com.ctv_it.klb.dto.baseInfo.CustomerInfoDTO;
import com.ctv_it.klb.dto.baseInfo.TransactionInfoDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchAccountDataResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchCustomerDataResponseDTO;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import com.ctv_it.klb.dto.filter.extend.TransactionFilterDTO;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.fetch.FetchAccountServiceFC;
import com.ctv_it.klb.service.fetch.FetchCustomerServiceFC;
import com.ctv_it.klb.service.fetch.FetchTransactionServiceFC;
import com.ctv_it.klb.service.type.ReportTypeService;
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
    log.info(
        "TransactionReportServiceImpl::search(accountId={}, reportFilterDTO={}) is processing",
        accountId, reportFilterDTO);

    TransactionFilterDTO transactionFilters = (TransactionFilterDTO) reportFilterDTO;

    CustomerInfoDTO customer = fetchCustomerByAccountId(accountId);
    AccountInfoDTO account = fetchAccountById(accountId);
    List<TransactionInfoDTO> transactions = search(account.getNumber(), transactionFilters);

    return TransactionReportDTO.builder()
        .customer(customer)
        .account(account)
        .transactions(transactions)
        .build();
  }

  private CustomerInfoDTO fetchCustomerByAccountId(long accountId) {
    log.info("Fetching customer data for accountId: {}", accountId);
    FetchCustomerDataResponseDTO data = fetchCustomerServiceFC.findByAccountId(accountId);
    CustomerInfoDTO customer = fetchCustomerServiceFC.map(data);
    log.info("Mapped customer data: {}", customer);
    return customer;
  }

  private AccountInfoDTO fetchAccountById(long accountId) {
    log.info("Fetching account by accountId: {}", accountId);
    FetchAccountDataResponseDTO data = fetchAccountServiceFC.getAccountById(accountId);
    AccountInfoDTO account = fetchAccountServiceFC.map(data);
    account.setNo(1L);
    account.setType("Tài khoản thanh toán");
    log.info("Mapped account data: {}", account);
    return account;
  }

  private List<TransactionInfoDTO> search(
      String accountNumber,
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

    return fetchTransactionServiceFC.search(
            accountNumber,
            transactionType,
            fromDate == null ? null : fromDate.toString(),
            toDate == null ? null : toDate.toString(),
            transactionStatus)
        .getTransactions();
  }
}
