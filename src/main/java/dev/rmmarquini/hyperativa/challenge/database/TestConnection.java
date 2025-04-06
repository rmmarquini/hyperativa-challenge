package dev.rmmarquini.hyperativa.challenge.database;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@NoArgsConstructor
public class TestConnection {

	private static final Logger logger = LoggerFactory.getLogger(TestConnection.class);

	// TODO: Remove this method after testing the connection with MongoDB
	@Bean
	public CommandLineRunner run(TestDBRepository repository) {
		return args -> {
			TestDB testDB = new TestDB();
			testDB.setDescription("Connection OK.");
			repository.save(testDB);
			logger.debug("Saved data: {}", repository.findAll());
		};
	}

}
