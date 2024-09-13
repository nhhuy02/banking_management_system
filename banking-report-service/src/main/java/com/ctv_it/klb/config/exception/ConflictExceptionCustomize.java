package com.ctv_it.klb.config.exception;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.response.ErrorDetailDTO;
import java.util.List;

public class ConflictExceptionCustomize extends BaseExceptionCustomize {

  public ConflictExceptionCustomize(List<ErrorDetailDTO> errors) {
    super(Translator.toLocale("error.existing"), errors);
  }
}
