package dev.rmmarquini.hyperativa.challenge.service;

import dev.rmmarquini.hyperativa.challenge.entity.CardEntity;
import dev.rmmarquini.hyperativa.challenge.repository.CardRepository;
import dev.rmmarquini.hyperativa.challenge.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class CardService {

	private static final Logger logger = LoggerFactory.getLogger(CardService.class);
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE; // YYYYMMDD

	@Autowired
	private CardRepository cardRepository;

	@Autowired
	private EncryptionService encryptionService;

	public CardEntity saveCard(String batchDescription, String batchDate, String batchNumber,
	                           String lineIdentifier, String batchSequenceNumber, String cardNumber) {

		String trimmedCardNumber = cardNumber.trim();
		logger.info("Saving card number: {}", StringUtils.maskCardNumber(trimmedCardNumber));

		CardEntity newCardEntity = CardEntity.builder()
				.batchDescription(batchDescription)
				.batchDate(LocalDate.parse(batchDate, DATE_FORMATTER))
				.batchNumber(batchNumber)
				.lineIdentifier(lineIdentifier)
				.batchSequenceNumber(batchSequenceNumber)
				.encryptedCardNumber(encryptionService.encrypt(trimmedCardNumber))
				.cardNumberHash(StringUtils.hashCardNumber(trimmedCardNumber))
				.build();

		logger.debug("Encrypted card number during save: {}", newCardEntity.getEncryptedCardNumber());

		CardEntity savedCardEntity = cardRepository.save(newCardEntity);
		logger.info("Card saved with ID: {}", savedCardEntity.getId());
		return savedCardEntity;

	}

	public Optional<CardEntity> findByCardNumber(String cardNumber) {
		if (cardNumber == null || cardNumber.trim().isEmpty()) {
			logger.error("Card number to search is null or empty");
			return Optional.empty();
		}
		String trimmedCardNumber = cardNumber.trim();
		logger.debug("Raw card number during search: '{}'", trimmedCardNumber);
		String cardNumberHash = StringUtils.hashCardNumber(trimmedCardNumber);
		logger.debug("Hashed card number during search: {}", cardNumberHash);
		Optional<CardEntity> cardEntity = cardRepository.findByCardNumberHash(cardNumberHash);
		if (cardEntity.isPresent()) {
			logger.debug("Found card with encrypted number: {}", cardEntity.get().getEncryptedCardNumber());
		} else {
			logger.debug("No card found for hash: {}", cardNumberHash);
		}
		return cardEntity;
	}

}
