package com.service.psychologists;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class PsychologistsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PsychologistsApiApplication.class, args);
	}

}
