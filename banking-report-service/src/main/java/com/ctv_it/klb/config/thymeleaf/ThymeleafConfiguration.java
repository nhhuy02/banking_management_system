package com.ctv_it.klb.config.thymeleaf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class ThymeleafConfiguration {

  @Bean
  public ClassLoaderTemplateResolver fileTemplateResolver() {
    ClassLoaderTemplateResolver fileTemplateResolver = new ClassLoaderTemplateResolver();
    fileTemplateResolver.setTemplateMode("HTML");
    fileTemplateResolver.setSuffix(".html");
    fileTemplateResolver.setCharacterEncoding("UTF-8");
    fileTemplateResolver.setOrder(1);
    return fileTemplateResolver;
  }

  @Bean
  public SpringTemplateEngine templateEngine() {
    SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
    springTemplateEngine.setEnableSpringELCompiler(true);
    springTemplateEngine.setTemplateResolver(fileTemplateResolver());
    return springTemplateEngine;
  }

  @Bean
  public ThymeleafViewResolver thymeleafViewResolver() {
    ThymeleafViewResolver resolver = new ThymeleafViewResolver();
    resolver.setTemplateEngine(templateEngine());
    resolver.setCharacterEncoding("UTF-8");
    return resolver;
  }
}
