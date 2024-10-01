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
public class LoanPDFServiceImpl implements ReportFormatService<LoanReportDTO> {

  @Override
  public byte[] export(String fileName, LoanReportDTO reportData) {
    log.info(Translator.toLocale("msg.called", "LoanPDFServiceImpl::export"));
    log.info("ReportData: {}", reportData);

    fileName += getFormat().getExtension(); // example: "fileLoan" + ".xlsx" = "fileLoan.pdf"

    return new byte[0];
  }

  @Override
  public ReportType getType() {
    return ReportType.LOAN;
  }

  @Override
  public ReportFormat getFormat() {
    return ReportFormat.PDF;
  }
}
