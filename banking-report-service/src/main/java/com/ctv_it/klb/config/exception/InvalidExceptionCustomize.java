package com.ctv_it.klb.config.exception;

import com.ctv_it.klb.config.i18n.Translator;
import java.util.Map;

public class InvalidExceptionCustomize extends BaseExceptionCustomize {

  public InvalidExceptionCustomize(String msg, Map<String, Object> errors) {
    super(msg, errors);
  }

  public InvalidExceptionCustomize(Map<String, Object> errors) {
    super(Translator.toLocale("error.invalid.data"), errors);
  }
}
