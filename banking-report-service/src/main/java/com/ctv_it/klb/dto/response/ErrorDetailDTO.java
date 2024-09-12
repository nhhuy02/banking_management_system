package com.ctv_it.klb.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ErrorDetailDTO {

  private String field;
  private Object rejectedValue;
  private String message;
}
