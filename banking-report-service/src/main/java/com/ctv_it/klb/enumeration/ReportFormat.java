package com.ctv_it.klb.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ReportFormat {
  NONE("none", "", "", "application/json"),
  PDF(".pdf", "/template/pdf", ".html", "application/pdf"),
  EXCEL(".xlsx", "/template/excel", ".xlsx", "application/octet-stream");

  private String extension;
  private String templatePath;
  private String templateExtension;
  private String headerContentType;

  public static ReportFormat fromString(String value) {
    try {
      return ReportFormat.valueOf(value.toUpperCase());
    } catch (Exception ex) {
      return null;
    }
  }
}
