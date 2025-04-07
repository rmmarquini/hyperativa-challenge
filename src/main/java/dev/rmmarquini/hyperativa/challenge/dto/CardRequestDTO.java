package dev.rmmarquini.hyperativa.challenge.dto;

public record CardRequestDTO (String batchDescription, String batchDate, String batchNumber,
                              String lineIdentifier, String batchSequenceNumber, String cardNumber) {
}
