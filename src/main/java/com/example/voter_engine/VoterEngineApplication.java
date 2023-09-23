package com.example.voter_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class VoterEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoterEngineApplication.class, args);
	}

}
