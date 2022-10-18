package com.godchigam.godchigam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GodchigamApplication {

	public static void main(String[] args) {
		SpringApplication.run(GodchigamApplication.class, args);
	}

}
