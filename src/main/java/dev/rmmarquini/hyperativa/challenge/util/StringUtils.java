package dev.rmmarquini.hyperativa.challenge.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.HexFormat;

public class StringUtils {

	private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);

	public static String maskCardNumber(String cardNumber) {
		return cardNumber.replaceAll("\\d(?=\\d{4})", "*"); // Mask all but the last 4 digits
	}

	public static String hashCardNumber(String cardNumber) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(cardNumber.getBytes());
			return HexFormat.of().formatHex(hash);
		} catch (Exception e) {
			logger.error("Failed to hash card number: {}", e.getMessage(), e);
			throw new RuntimeException("Hashing failed", e);
		}
	}

}
