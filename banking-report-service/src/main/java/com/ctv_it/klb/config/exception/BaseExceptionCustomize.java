package com.ctv_it.klb.config.exception;

import com.ctv_it.klb.dto.response.ErrorDetailDTO;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class BaseExceptionCustomize extends RuntimeException {

  protected List<ErrorDetailDTO> errors;

  public BaseExceptionCustomize(String msg, List<ErrorDetailDTO> errors) {
    super(msg);
    this.errors = errors;
  }
}
