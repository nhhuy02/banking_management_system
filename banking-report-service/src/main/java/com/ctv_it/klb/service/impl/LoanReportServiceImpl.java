package com.ctv_it.klb.service.impl;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.LoanReportDTO;
import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoanReportServiceImpl implements ReportService<LoanReportDTO> {

  @Override
  public LoanReportDTO report(ReportRequestDTO reportRequestDTO) {
    log.info(Translator.toLocale("CALLED", "LoanReportServiceImpl"));
    return null;
  }

  @Override
  public ReportType getReportType() {
    return ReportType.LOAN;
  }
}
