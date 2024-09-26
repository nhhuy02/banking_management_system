package com.ctv_it.klb.service.format.impl;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.LoanReportDTO;
import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.format.ReportFormatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoanExcelServiceImpl implements ReportFormatService<LoanReportDTO> {

  @Override
  public Byte[] export(LoanReportDTO reportData) {
    log.info(Translator.toLocale("msg.called", "LoanExcelServiceImpl::export"));
    log.info("ReportData: {}", reportData);

    return new Byte[0];
  }

  @Override
  public ReportType getType() {
    return ReportType.LOAN;
  }

  @Override
  public ReportFormat getFormat() {
    return ReportFormat.EXCEL;
  }
}
