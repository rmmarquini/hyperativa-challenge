package dev.rmmarquini.hyperativa.challenge.controller;

import dev.rmmarquini.hyperativa.challenge.dto.CardRequestDTO;
import dev.rmmarquini.hyperativa.challenge.dto.CardResponseDTO;
import dev.rmmarquini.hyperativa.challenge.entity.CardEntity;
import dev.rmmarquini.hyperativa.challenge.service.CardService;
import dev.rmmarquini.hyperativa.challenge.service.EncryptionService;
import dev.rmmarquini.hyperativa.challenge.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/cards")
public class CardController {

	private static final Logger logger = LoggerFactory.getLogger(CardController.class);

	@Autowired
	private CardService cardService;

	@Autowired
	private EncryptionService encryptionService;

	@PostMapping
	public ResponseEntity<CardResponseDTO> createCard(@RequestBody CardRequestDTO cardRequestDTO) {
		logger.info("Request to save the received card: {}", cardRequestDTO);
		CardEntity cardEntity = cardService.saveCard(
				cardRequestDTO.batchDescription(),
				cardRequestDTO.batchDate(),
				cardRequestDTO.batchNumber(),
				cardRequestDTO.lineIdentifier(),
				cardRequestDTO.batchSequenceNumber(),
				cardRequestDTO.cardNumber()
		);
		CardResponseDTO cardResponseDTO = new CardResponseDTO(
				cardEntity.getId(),
				StringUtils.maskCardNumber(encryptionService.decrypt(cardEntity.getEncryptedCardNumber()))
		);
		logger.info("Card saved successfully: {}", cardResponseDTO);

		return ResponseEntity.created(URI.create("/cards")).body(cardResponseDTO);

	}

}
