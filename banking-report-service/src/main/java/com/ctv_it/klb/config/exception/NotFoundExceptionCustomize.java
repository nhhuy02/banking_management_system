package com.ctv_it.klb.config.exception;


import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.response.ErrorDetailDTO;
import java.util.List;

public class NotFoundExceptionCustomize extends BaseExceptionCustomize {

  public NotFoundExceptionCustomize(List<ErrorDetailDTO> errors) {
    super(Translator.toLocale("error.not-found"), errors);
  }

}
