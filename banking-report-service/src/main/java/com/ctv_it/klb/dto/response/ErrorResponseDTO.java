package com.ctv_it.klb.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Setter
@Getter
@ToString(callSuper = true)
public class ErrorResponseDTO extends BaseResponseDTO {

  private String message;
  private List<ErrorDetailDTO> errors;
}
