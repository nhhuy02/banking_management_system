package com.ctv_it.klb.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ReportFormat {
  NONE("none", "", ""),
  PDF(".pdf", "/template/pdf", ".html"),
  EXCEL(".xlsx", "/template/excel", ".xlsx");

  private String extension;
  private String templatePath;
  private String templateExtension;

  public static ReportFormat fromString(String value) {
    try {
      return ReportFormat.valueOf(value.toUpperCase());
    } catch (Exception ex) {
      return null;
    }
  }
}
