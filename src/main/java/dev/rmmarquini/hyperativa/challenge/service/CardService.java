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

		logger.info("Saving card with masked number: {}", StringUtils.maskCardNumber(cardNumber));

		CardEntity newCardEntity = CardEntity.builder()
				.batchDescription(batchDescription)
				.batchDate(LocalDate.parse(batchDate, DATE_FORMATTER))
				.batchNumber(batchNumber)
				.lineIdentifier(lineIdentifier)
				.batchSequenceNumber(batchSequenceNumber)
				.encryptedCardNumber(encryptionService.encrypt(cardNumber))
				.build();

		CardEntity savedCardEntity = cardRepository.save(newCardEntity);
		logger.info("Card saved with ID: {}", savedCardEntity.getId());
		return savedCardEntity;

	}

	public Optional<CardEntity> findByCardNumber(String cardNumber) {
		logger.info("Finding card with masked number: {}", StringUtils.maskCardNumber(cardNumber));
		String encryptedCardNumber = encryptionService.encrypt(cardNumber);
		return cardRepository.findByEncryptedCardNumber(encryptedCardNumber);
	}

}
