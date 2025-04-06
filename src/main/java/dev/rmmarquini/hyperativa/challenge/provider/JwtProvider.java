package dev.rmmarquini.hyperativa.challenge.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtProvider {

	private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
	private static final String TOKEN_PREFIX = "Bearer ";

	@Value("${jwt.secret}")
	private String secret;

	public DecodedJWT validateToken(String token) {
		token = token.replace(TOKEN_PREFIX, "");
		Algorithm algorithm = Algorithm.HMAC512(secret);
		try {
			DecodedJWT decodedJWT = JWT.require(algorithm)
					.build()
					.verify(token);
			logger.info("Token validated successfully: {}", decodedJWT.getSubject());
			return decodedJWT;
		} catch (JWTVerificationException e) {
			logger.error("Invalid JWT token {}: {}.", token, e.getMessage());
			return null;
		}
	}

}
