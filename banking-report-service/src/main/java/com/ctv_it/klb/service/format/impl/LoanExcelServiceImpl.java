package com.ctv_it.klb.service.format.impl;

import com.ctv_it.klb.dto.LoanReportDTO;
import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.format.ReportFormatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoanExcelServiceImpl implements ReportFormatService<LoanReportDTO> {

  @Override
  public ReportType getType() {
    return ReportType.LOAN;
  }

  @Override
  public ReportFormat getFormat() {
    return ReportFormat.EXCEL;
  }

  @Override
  public Resource export(LoanReportDTO reportData) {
    log.info("LoanExcelServiceImpl::export is processing with data: {}", reportData);

    return null;
  }
}
