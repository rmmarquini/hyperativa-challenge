package dev.rmmarquini.hyperativa.desafio.database;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestDBRepository extends MongoRepository<TestDB, String> {
}
