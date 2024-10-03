package com.ctv_it.klb.service.format.impl;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.TransactionReportDTO;
import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.format.ReportFormatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransactionExcelServiceImpl implements ReportFormatService<TransactionReportDTO> {

  @Override
  public byte[] export(TransactionReportDTO reportData) {
    log.info("TransactionExcelServiceImpl::export is processing with data: {}", reportData);

    return new byte[0];
  }

  @Override
  public ReportType getType() {
    return ReportType.TRANSACTION;
  }

  @Override
  public ReportFormat getFormat() {
    return ReportFormat.EXCEL;
  }
}
