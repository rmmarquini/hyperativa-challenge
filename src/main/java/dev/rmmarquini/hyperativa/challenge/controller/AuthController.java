package dev.rmmarquini.hyperativa.challenge.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Value("${jwt.secret}")
	private String secret;

	@GetMapping("/token")
	public String getToken() {
		LocalDateTime now = LocalDateTime.now();
		Instant issuedAt = now.atZone(ZoneId.systemDefault()).toInstant();
		Instant expiresAt = now.plus(Duration.ofHours(1)).atZone(ZoneId.systemDefault()).toInstant();

		return JWT.create()
				.withSubject("test-user")
				.withIssuedAt(issuedAt)
				.withExpiresAt(expiresAt)
				.sign(Algorithm.HMAC256(secret));
	}

}
