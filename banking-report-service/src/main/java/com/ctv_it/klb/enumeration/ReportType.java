package com.ctv_it.klb.enumeration;

public enum ReportType {
  ACCOUNT, TRANSACTION, LOAN;

  public static ReportType fromString(String value) {
    try {
      return ReportType.valueOf(value);
    } catch (Exception ex) {
      return null;
    }
  }
}
