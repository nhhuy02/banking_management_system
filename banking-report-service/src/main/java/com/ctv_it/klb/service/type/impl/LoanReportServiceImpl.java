package com.ctv_it.klb.service.type.impl;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.LoanReportDTO;
import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.type.ReportTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoanReportServiceImpl implements ReportTypeService<LoanReportDTO> {

  @Override
  public LoanReportDTO search(long accountId, ReportRequestDTO reportRequestDTO) {
    log.info("LoanReportServiceImpl::search is processing with request: {}", reportRequestDTO);

    return null;
  }

  @Override
  public ReportType getType() {
    return ReportType.LOAN;
  }
}
