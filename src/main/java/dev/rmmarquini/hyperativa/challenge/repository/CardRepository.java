package dev.rmmarquini.hyperativa.challenge.repository;

import dev.rmmarquini.hyperativa.challenge.entity.CardEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CardRepository extends MongoRepository<CardEntity, String> {
	Optional<CardEntity> findByEncryptedCardNumber(String encryptedCardNumber);
	Optional<CardEntity> findByCardNumberHash(String cardNumberHash);
}
