package com.org.gunbbang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GunbbangApplication {

	public static void main(String[] args) {
		SpringApplication.run(GunbbangApplication.class, args);
	}
}
