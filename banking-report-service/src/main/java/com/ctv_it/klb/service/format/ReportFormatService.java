package com.ctv_it.klb.service.format;

import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.enumeration.ReportType;
import java.time.LocalDateTime;
import org.springframework.core.io.Resource;

public interface ReportFormatService<T> {

  ReportType getType();

  ReportFormat getFormat();

  Resource export(T data);

  // file template example: /template/excel/account.xlsx
  default String getTemplateFile() {
    return getFormat().getTemplatePath() + "/" + getType().name().toLowerCase()
        + getFormat().getTemplateExtension();
  }

  // file name example: ACCOUNT_EXCEL_2024-10-02T17:27:10.446019.xlsx
  default String generateFileName() {
    return getType().name().toLowerCase() + "_" + getFormat().name().toLowerCase() + "_"
        + LocalDateTime.now() + getFormat().getExtension();
  }

}
