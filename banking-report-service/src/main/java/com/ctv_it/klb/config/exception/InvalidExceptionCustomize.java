package com.ctv_it.klb.config.exception;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.response.ErrorDetailDTO;
import java.util.List;

public class InvalidExceptionCustomize extends BaseExceptionCustomize {

  public InvalidExceptionCustomize(String msg, List<ErrorDetailDTO> errors) {
    super(msg, errors);
  }

  public InvalidExceptionCustomize(List<ErrorDetailDTO> errors) {
    super(Translator.toLocale("error.invalid.data"), errors);
  }
}
