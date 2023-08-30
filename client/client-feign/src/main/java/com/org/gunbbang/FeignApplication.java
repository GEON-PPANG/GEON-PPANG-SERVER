package com.org.gunbbang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FeignApplication {
  public static void main(String[] args) {
    SpringApplication.run(FeignApplication.class, args);
  }
}
