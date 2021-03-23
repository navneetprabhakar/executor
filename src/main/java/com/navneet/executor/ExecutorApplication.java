package com.navneet.executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAutoConfiguration
@EnableJpaRepositories("com.navneet.executor")
@EntityScan("com.navneet.executor")
public class ExecutorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExecutorApplication.class, args);
	}

}
