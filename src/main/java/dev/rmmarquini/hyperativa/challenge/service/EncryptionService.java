package dev.rmmarquini.hyperativa.challenge.service;

import org.jasypt.encryption.StringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

	private static final Logger logger = LoggerFactory.getLogger(EncryptionService.class);

	@Autowired
	private StringEncryptor stringEncryptor;

	public EncryptionService() {
		logger.info("EncryptionService instance created: {}", this.hashCode());
	}

	public String encrypt(String data) {
		logger.info("Encrypting with StringEncryptor instance: {}", stringEncryptor.hashCode());
		return stringEncryptor.encrypt(data);
	}

	public String decrypt(String encryptedData) {
		logger.info("Decrypting with StringEncryptor instance: {}", stringEncryptor.hashCode());
		return stringEncryptor.decrypt(encryptedData);
	}

}
