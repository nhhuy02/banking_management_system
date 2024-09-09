package com.ctv_it.klb.config.exception;


import com.ctv_it.klb.config.i18n.Translator;

public class NotFoundExceptionCustomize extends BaseExceptionCustomize {

  public NotFoundExceptionCustomize(Object errors) {
    super(Translator.toLocale("error.not-found-1"), errors);
  }

}
