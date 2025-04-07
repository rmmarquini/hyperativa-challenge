package dev.rmmarquini.hyperativa.challenge.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "cards")
public class CardEntity {

	@Id
	private String id;
	private String batchDescription; // Batch description (e.g., "DESAFIO-HYPERATIVA")
	private LocalDate batchDate; // Batch date (e.g., "20180524")
	private String batchNumber; // Batch number (e.g., "0001")
	private String lineIdentifier; // Line identifier (e.g., "C")
	private int batchSequenceNumber; // Sequence number in batch (e.g., 2)
	private String encryptedCardNumber; // AES-256 fully encrypted card number (e.g., "4456897999999999")

}
