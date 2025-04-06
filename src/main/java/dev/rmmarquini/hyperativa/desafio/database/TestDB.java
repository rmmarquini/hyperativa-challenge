package dev.rmmarquini.hyperativa.desafio.database;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "test")
public class TestDB {

	@Id
	private String id;
	private String description;

}
