package com.ctv_it.klb.service.format;

import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.enumeration.ReportType;
import org.apache.poi.ss.formula.functions.T;

public interface ReportFormatService<T> {

  byte[] export(String fileName, T data);

  ReportType getType();

  ReportFormat getFormat();
}
