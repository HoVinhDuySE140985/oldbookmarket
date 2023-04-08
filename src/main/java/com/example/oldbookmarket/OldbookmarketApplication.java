package com.example.oldbookmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OldbookmarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(OldbookmarketApplication.class, args);
	}

}
