package com.ctv_it.klb.enumeration;

import lombok.Getter;

@Getter
public enum ReportType {
  ACCOUNT(), TRANSACTION(), LOAN();

  public static ReportType fromString(String value) {
    try {
      return ReportType.valueOf(value.toUpperCase());
    } catch (Exception ex) {
      return null;
    }
  }
}
