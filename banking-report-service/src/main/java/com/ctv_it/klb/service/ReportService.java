package com.ctv_it.klb.service;

import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.enumeration.ReportType;

public interface ReportService<T> {

  T report(ReportRequestDTO reportRequestDTO);

  ReportType getReportType();
}
