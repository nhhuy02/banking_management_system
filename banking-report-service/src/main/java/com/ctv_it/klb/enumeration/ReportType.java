package com.ctv_it.klb.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ReportType {
  ACCOUNT("account"), TRANSACTION("transaction"), LOAN("loan");

  private String value;

  public static ReportType fromString(String value) {
    try {
      return ReportType.valueOf(value.toUpperCase());
    } catch (Exception ex) {
      return null;
    }
  }
}
