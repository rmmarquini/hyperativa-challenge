package dev.rmmarquini.hyperativa.desafio;

import dev.rmmarquini.hyperativa.desafio.database.TestDB;
import dev.rmmarquini.hyperativa.desafio.database.TestDBRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DesafioHyperativaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioHyperativaApplication.class, args);
	}

}
