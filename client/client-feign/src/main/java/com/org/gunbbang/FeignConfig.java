package com.org.gunbbang;

import feign.Logger;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients
@Configuration
public class FeignConfig {
  @Bean
  Logger.Level loggerLevel() {
    return Logger.Level.FULL;
  }
}
