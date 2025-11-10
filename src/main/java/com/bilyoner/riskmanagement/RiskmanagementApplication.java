package com.bilyoner.riskmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RiskmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(RiskmanagementApplication.class, args);
	}

}
