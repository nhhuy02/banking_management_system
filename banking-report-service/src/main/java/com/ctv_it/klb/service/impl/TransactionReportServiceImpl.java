package com.ctv_it.klb.service.impl;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.base.TransactionReportDTO;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionReportServiceImpl implements ReportService<TransactionReportDTO> {

  @Override
  public TransactionReportDTO generate(ReportFilterDTO filterDTO) {
    log.info(Translator.toLocale("CALLED", "TransactionReportServiceImpl"));
    return null;
  }

  @Override
  public ReportType getReportType() {
    return ReportType.TRANSACTION;
  }
}
