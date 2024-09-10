package com.ctv_it.klb.config.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class Translator {

  private static ResourceBundleMessageSource messageSource;

  private Translator(@Autowired ResourceBundleMessageSource messageSource) {
    Translator.messageSource = messageSource;
  }

  public static String toLocale(String msgCode) {
    return toLocale(msgCode, (Object) null);
  }

  public static String toLocale(String msgCode, Object... args) {
    return messageSource.getMessage(msgCode, args, LocaleContextHolder.getLocale());
  }
}
