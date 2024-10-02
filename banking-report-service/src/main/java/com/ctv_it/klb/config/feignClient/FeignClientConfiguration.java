package com.ctv_it.klb.config.feignClient;

import com.ctv_it.klb.config.mapper.CustomErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {

  @Bean
  public ErrorDecoder errorDecoder() {
    return new CustomErrorDecoder();
  }
}
