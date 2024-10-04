package com.ctv_it.klb.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum BaseStatus {

  ACTIVE("Đang hoạt động"),
  SUSPENDED("Đang treo"),
  CLOSED("Đã đóng");
  private String value;
}
