package com.ctv_it.klb.config.i18n;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageSourceConfiguration {

  @Bean
  public ResourceBundleMessageSource messageSource() {
    ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
    rs.setBasename("i18n/messages");
    rs.setDefaultEncoding("UTF-8");
    rs.setUseCodeAsDefaultMessage(true);
    rs.setCacheSeconds(3600);
    return rs;
  }
}
