package com.ttdat.debtservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories
public class DebtServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DebtServiceApplication.class, args);
	}

}
