package com.ctv_it.klb.service.format.impl;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.AccountReportDTO;
import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.format.ReportFormatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountPDFServiceImpl implements ReportFormatService<AccountReportDTO> {

  @Override
  public byte[] export(String fileName, AccountReportDTO reportData) {
    log.info(Translator.toLocale("msg.called", "AccountPDFServiceImpl::export"));
    log.info("ReportData: {}", reportData);

    fileName += getFormat().getExtension(); // example: "fileAccount" + ".xlsx" = "fileAccount.pdf"

    return new byte[0];
  }

  @Override
  public ReportType getType() {
    return ReportType.ACCOUNT;
  }

  @Override
  public ReportFormat getFormat() {
    return ReportFormat.PDF;
  }
}
