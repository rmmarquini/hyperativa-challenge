package dev.rmmarquini.hyperativa.challenge.config;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Security;

@Configuration
public class JasyptConfig {

	@Value("${jasypt.encryptor.password}")
	private String encryptorPassword;

	@Bean(name = "jasyptStringEncryptor")
	public StringEncryptor stringEncryptor() {
		Security.addProvider(new BouncyCastleProvider());
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setProviderName("BC");
		encryptor.setPassword(encryptorPassword);
		encryptor.setAlgorithm("PBEWithSHA256And256BitAES-CBC-BC");
		return encryptor;
	}
}
