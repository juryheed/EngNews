package org.mjulikelion.engnews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EngNewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EngNewsApplication.class, args);
	}

}
