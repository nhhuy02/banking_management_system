package com.ctv_it.klb.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class ReportExportRequestDTO {

  private String reportType;
  private String reportFormat;
  private Object data;
}
