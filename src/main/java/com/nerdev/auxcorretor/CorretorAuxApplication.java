package com.nerdev.auxcorretor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CorretorAuxApplication {

	public static void main(String[] args) {
		SpringApplication.run(CorretorAuxApplication.class, args);
	}

}
