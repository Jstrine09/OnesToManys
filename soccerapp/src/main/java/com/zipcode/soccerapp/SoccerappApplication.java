package com.zipcode.soccerapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SoccerappApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoccerappApplication.class, args);
	}

	@Bean
	public CommandLineRunner openBrowser() {
		return args -> {
			Thread.sleep(5000); // give the server a moment to start
			Runtime.getRuntime().exec(new String[]{"open", "http://localhost:8080"});
		};
	}
}
