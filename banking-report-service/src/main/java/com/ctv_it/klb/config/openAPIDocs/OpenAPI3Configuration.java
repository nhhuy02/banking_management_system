package com.ctv_it.klb.config.openAPIDocs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Collections;
import java.util.List;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPI3Configuration {

  @Value("${openapi.service.api-docs}")
  private String apiDocs;

  @Value("${openapi.service.title}")
  private String title;

  @Value("${openapi.service.version}")
  private String version;

  @Value("${openapi.service.description}")
  private String description;

  @Value("${openapi.service.serverUrl}")
  private String serverUrl;

  @Value("${openapi.service.serverName}")
  private String serverName;

  @Bean
  public GroupedOpenApi groupedOpenAPIConfig() {
    return GroupedOpenApi.builder()
        .group(apiDocs)
        .packagesToScan("com.ctv_it.klb.controller")
        .build();
  }

  @Bean
  public OpenAPI openAPIConfig() {
    return new OpenAPI()
        .info(infoConfig())
        .servers(serversConfig())
        .components(componentsConfig());
  }

  private Info infoConfig() {
    return new Info()
        .title(title)
        .version(version)
        .description(description)
        .license(new License().name("API License").url("http://domain.vn/license"));
  }

  private List<Server> serversConfig() {
    return Collections.singletonList(
        new Server().url(serverUrl).description(serverName)
    );
  }

  private Components componentsConfig() {
    return new Components()
        .addParameters("Accept-Language", languageAcceptHeaderParameterConfig())
        .addSecuritySchemes("bearerAuth", bearerAuthHeaderSecuritySchemeConfig());
  }

  private Parameter languageAcceptHeaderParameterConfig() {
    return new Parameter()
        .name("Accept-Language")
        .description("Language to be used for the response")
        .required(false)
        .in("header")
        .schema(new io.swagger.v3.oas.models.media.StringSchema()
            ._default("en")
            .addEnumItem("en")
            .addEnumItem("vi"));
  }

  private SecurityScheme bearerAuthHeaderSecuritySchemeConfig() {
    return new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT");
  }
}
