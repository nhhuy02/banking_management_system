package com.ctv_it.klb.service.type.impl;

import com.ctv_it.klb.dto.LoanReportDTO;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import com.ctv_it.klb.dto.filter.extend.LoanFilterDTO;
import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.type.ReportTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoanReportServiceImpl implements ReportTypeService<LoanReportDTO> {

  @Override
  public ReportType getType() {
    return ReportType.LOAN;
  }

  @Override
  public LoanReportDTO search(Long accountId, ReportFilterDTO reportFilterDTO) {
    log.info(
        "LoanReportServiceImpl::search(accountId={}, reportFilterDTO={}) is processing",
        accountId, reportFilterDTO);

    return null;
  }
}
