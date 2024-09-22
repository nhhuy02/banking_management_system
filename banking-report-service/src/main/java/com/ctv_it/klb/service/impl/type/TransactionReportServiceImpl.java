package com.ctv_it.klb.service.impl.type;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.TransactionReportDTO;
import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.ReportTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionReportServiceImpl implements ReportTypeService<TransactionReportDTO> {

  @Override
  public TransactionReportDTO search(ReportRequestDTO reportRequestDTO) {
    log.info(Translator.toLocale("msg.called", "TransactionReportServiceImpl::search"));

    return null;
  }

  @Override
  public ReportType getType() {
    return ReportType.TRANSACTION;
  }
}
