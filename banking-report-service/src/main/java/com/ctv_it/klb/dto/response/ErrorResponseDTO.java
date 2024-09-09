package com.ctv_it.klb.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@SuperBuilder
@Getter
@Setter
@ToString(callSuper = true)
public class ErrorResponseDTO extends BaseResponseDTO {
  private Object details;
}
