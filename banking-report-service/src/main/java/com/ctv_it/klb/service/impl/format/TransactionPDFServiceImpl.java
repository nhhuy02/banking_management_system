package com.ctv_it.klb.service.impl.format;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.TransactionReportDTO;
import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.service.ReportFormatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransactionPDFServiceImpl implements ReportFormatService<TransactionReportDTO> {

  @Override
  public Byte[] export(TransactionReportDTO reportData) {
    log.info(Translator.toLocale("msg.called", "TransactionPDFServiceImpl::export"));
    log.info("ReportData: {}", reportData);

    return new Byte[0];
  }

  @Override
  public ReportFormat getFormat() {
    return ReportFormat.PDF;
  }
}
