package com.org.gunbbang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {"com.org.gunbbang"})
@EnableJpaAuditing
@EnableConfigurationProperties
public class GunbbangApplication {

  public static void main(String[] args) {
    SpringApplication.run(GunbbangApplication.class, args);
  }
}
