package dev.rmmarquini.hyperativa.challenge.database;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestDBRepository extends MongoRepository<TestDB, String> {
}
