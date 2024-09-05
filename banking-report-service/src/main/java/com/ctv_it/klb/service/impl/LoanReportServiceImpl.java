package com.ctv_it.klb.service.impl;

import com.ctv_it.klb.dto.LoanReportDTO;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.ReportService;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoanReportServiceImpl implements ReportService<LoanReportDTO> {

  @Override
  public LoanReportDTO generate(Map<String, Object> filters) {
    log.info("LoanReportServiceImpl called");
    return null;
  }

  @Override
  public ReportType getReportType() {
    return ReportType.LOAN;
  }
}
