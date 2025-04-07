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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

		return ResponseEntity.created(URI.create("/cards/".concat(cardResponseDTO.getId()))).body(cardResponseDTO);

	}

	@PostMapping("/upload")
	public ResponseEntity<List<CardResponseDTO>> uploadCards(@RequestParam("file") MultipartFile file) {
		logger.info("Upload cards from file: {}", file.getOriginalFilename());

		if (file.isEmpty()) {
			logger.warn("Uploaded file is empty");
			return ResponseEntity.badRequest().body(null);
		}

		List<CardResponseDTO> responses = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

			String line = reader.readLine();
			if (line == null || line.trim().isEmpty()) {
				logger.warn("Uploaded file is empty");
				return ResponseEntity.badRequest().body(null);
			}

			// parsing header
			String batchDescription = line.substring(0, 29).trim();
			String batchDate = line.substring(29, 37).trim();
			String batchNumber = line.substring(41, 45).trim(); // LOTE0001 -> 0001

			int lineNumber = 1;
			while ( (line = reader.readLine()) != null ) {
				lineNumber++;
				line = line.trim();
				if (line.isEmpty() || line.startsWith("LOTE")) {
					continue; // Skip empty lines or the footer
				}

				try {
					String lineIdentifier = line.substring(0, 1);
					String batchSequenceNumber = line.substring(1, 7).trim();
					String cardNumber = line.substring(7).trim();

					CardEntity cardEntity = cardService.saveCard(
							batchDescription,
							batchDate,
							batchNumber,
							lineIdentifier,
							batchSequenceNumber,
							cardNumber
					);

					responses.add(new CardResponseDTO(
							cardEntity.getId(),
							StringUtils.maskCardNumber(encryptionService.decrypt(cardEntity.getEncryptedCardNumber()))
					));

					logger.info("Processed card at line {}: {}", lineNumber, cardEntity.getId());

				} catch (Exception e) {
					logger.error("Error processing line {}: {}", lineNumber, e.getMessage());
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
				}

			}

		} catch (IOException e) {
			logger.error("Error reading uploaded file: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		logger.info("Upload completed with {} cards processed", responses.size());
		return ResponseEntity.created(URI.create("/cards/upload")).body(responses);

	}

}
