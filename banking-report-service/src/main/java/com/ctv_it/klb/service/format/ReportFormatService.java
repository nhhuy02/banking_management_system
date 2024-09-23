package com.ctv_it.klb.service.format;

import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.enumeration.ReportType;

public interface ReportFormatService<T> {

  Byte[] export(T reportData);

  ReportType getType();

  ReportFormat getFormat();
}
