package com.ctv_it.klb.config.exception;

import com.ctv_it.klb.config.i18n.Translator;
import java.util.Map;

public class ConflictExceptionCustomize extends BaseExceptionCustomize {

  public ConflictExceptionCustomize(Map<String, Object> errors) {
    super(Translator.toLocale("error.existing-1"), errors);
  }
}
