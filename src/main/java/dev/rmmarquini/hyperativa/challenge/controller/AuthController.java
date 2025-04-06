package dev.rmmarquini.hyperativa.challenge.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Value("${jwt.secret}")
	private String secret;

	@PostMapping("/token")
	public String generateToken(@RequestParam String username) {
		if (username == null || username.trim().isEmpty()) {
			return "Username is required";
		}

		LocalDateTime now = LocalDateTime.now();
		Instant issuedAt = now.atZone(ZoneId.systemDefault()).toInstant();
		Instant expiresAt = now.plus(Duration.ofHours(1)).atZone(ZoneId.systemDefault()).toInstant();

		return JWT.create()
				.withSubject(username)
				.withIssuedAt(issuedAt)
				.withExpiresAt(expiresAt)
				.sign(Algorithm.HMAC256(secret));
	}

}
