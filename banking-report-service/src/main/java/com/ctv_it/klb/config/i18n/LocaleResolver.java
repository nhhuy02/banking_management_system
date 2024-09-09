package com.ctv_it.klb.config.i18n;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class LocaleResolver extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {

  List<Locale> LOCALES = List.of(Locale.of("en"), Locale.of("vi"));

  @Override
  public Locale resolveLocale(HttpServletRequest request) {
    String languageHeader = request.getHeader("Accept-Language");
    return !StringUtils.hasLength(languageHeader)
        ? Locale.US
        : Locale.lookup(Locale.LanguageRange.parse(languageHeader), LOCALES);
  }

}
