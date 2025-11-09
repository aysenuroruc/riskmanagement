package com.bilyoner.riskmanagement;

import org.springframework.boot.SpringApplication;

public class TestRiskmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.from(RiskmanagementApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
