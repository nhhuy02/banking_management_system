package com.ctv_it.klb.service.type.impl;

import com.ctv_it.klb.dto.TransactionReportDTO;
import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.type.ReportTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionReportServiceImpl implements ReportTypeService<TransactionReportDTO> {

  @Override
  public TransactionReportDTO search(Long accountId, ReportRequestDTO reportRequestDTO) {
    log.info("TransactionReportServiceImpl::search is processing with request: {}",
        reportRequestDTO);

    return null;
  }

  @Override
  public ReportType getType() {
    return ReportType.TRANSACTION;
  }
}
