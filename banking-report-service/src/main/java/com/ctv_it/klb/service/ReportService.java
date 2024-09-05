package com.ctv_it.klb.service;

import com.ctv_it.klb.enumeration.ReportType;
import java.util.Map;

public interface ReportService<T> {

  T generate(Map<String, Object> filters);

  ReportType getReportType();
}
