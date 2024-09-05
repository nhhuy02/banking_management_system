package com.ctv_it.klb.dto.request;

import java.util.Map;
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
public class ReportRequestDTO {

  private String type;
  private Map<String, Object> filters;
  private Map<String, String> sorting;
}
