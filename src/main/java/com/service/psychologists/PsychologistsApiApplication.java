package com.service.psychologists;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
@Log
public class PsychologistsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PsychologistsApiApplication.class, args);
		log.info("Psychologists API started");
	}

}
