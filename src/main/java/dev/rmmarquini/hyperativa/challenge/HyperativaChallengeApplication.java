package dev.rmmarquini.hyperativa.challenge;

import dev.rmmarquini.hyperativa.challenge.database.TestConnection;
import dev.rmmarquini.hyperativa.challenge.database.TestDB;
import dev.rmmarquini.hyperativa.challenge.database.TestDBRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HyperativaChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(HyperativaChallengeApplication.class, args);
	}

}
