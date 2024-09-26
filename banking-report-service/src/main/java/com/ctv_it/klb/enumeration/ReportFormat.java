package com.ctv_it.klb.enumeration;

import lombok.Getter;

@Getter
public enum ReportFormat {
  NONE, PDF, EXCEL;

  public static ReportFormat fromString(String value) {
    try {
      return ReportFormat.valueOf(value.toUpperCase());
    } catch (Exception ex) {
      return null;
    }
  }
}
