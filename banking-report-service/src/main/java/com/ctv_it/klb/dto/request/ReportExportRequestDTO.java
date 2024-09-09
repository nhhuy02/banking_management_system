package com.ctv_it.klb.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReportExportRequestDTO {

  private String reportType;
  private String reportFormat;
  private Object data;
}
