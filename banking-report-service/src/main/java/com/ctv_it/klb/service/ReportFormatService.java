package com.ctv_it.klb.service;

import com.ctv_it.klb.enumeration.ReportFormat;

public interface ReportFormatService<T> {

  Byte[] export(T reportData);

  ReportFormat getFormat();
}
