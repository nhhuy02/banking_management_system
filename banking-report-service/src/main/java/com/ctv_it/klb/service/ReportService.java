package com.ctv_it.klb.service;

import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import com.ctv_it.klb.enumeration.ReportType;

public interface ReportService<T> {

  T generate(ReportFilterDTO filterDTO);

  ReportType getReportType();
}
