package com.ctv_it.klb.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ReportFormat {
  NONE(""), PDF(".pdf"), EXCEL(".xlsx");

  private String extension;

  public static ReportFormat fromString(String value) {
    try {
      return ReportFormat.valueOf(value.toUpperCase());
    } catch (Exception ex) {
      return null;
    }
  }
}
