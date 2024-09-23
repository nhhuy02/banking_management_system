package com.ctv_it.klb.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class ErrorDetailDTO {

  private String field;
  private Object rejectedValue;
  private String message;
}
