package com.ctv_it.klb.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@ToString(callSuper = true)
public class ErrorResponseDTO extends BaseResponseDTO {

  private List<ErrorDetailDTO> errors;
}
