package dev.rmmarquini.hyperativa.challenge.util;

public class StringUtils {

	public static String maskCardNumber(String cardNumber) {
		return cardNumber.replaceAll("\\d(?=\\d{4})", "*"); // Mask all but the last 4 digits
	}

}
