package com.ctv_it.klb.config.thymeleaf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class ThymeleafConfiguration {

  @Bean
  public ITemplateResolver xmlTemplateResolver() {
    final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setCacheable(false);
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setSuffix(".html");
    templateResolver.setCharacterEncoding("UTF-8");
    templateResolver.setOrder(1);
    return templateResolver;
  }

  @Bean
  public TemplateEngine templateProcessorEngine() {
    final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setEnableSpringELCompiler(true);
    templateEngine.addTemplateResolver(xmlTemplateResolver());
    return templateEngine;
  }
}
