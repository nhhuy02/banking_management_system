package com.ctv_it.klb.service.format.impl;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.TransactionReportDTO;
import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.format.ReportFormatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransactionExcelServiceImpl implements ReportFormatService<TransactionReportDTO> {

  @Override
  public ReportType getType() {
    return ReportType.TRANSACTION;
  }

  @Override
  public ReportFormat getFormat() {
    return ReportFormat.EXCEL;
  }

  @Override
  public Resource export(TransactionReportDTO data) {
    log.info("Export(type={}, format={}) is processing for data: {}", getType(), getFormat(), data);

    return null;
  }
}
