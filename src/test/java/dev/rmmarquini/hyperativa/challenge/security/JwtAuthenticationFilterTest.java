package dev.rmmarquini.hyperativa.challenge.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.rmmarquini.hyperativa.challenge.provider.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtAuthenticationFilterTest {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilterTest.class);

	@InjectMocks
	private JwtAuthenticationFilter filter;

	@Mock
	private JwtProvider jwtProvider;

	private static final String SECRET = "hyperativa-challenge";

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		SecurityContextHolder.clearContext();
		logger.info(new String(new char[25]).replace("\0", "-"));
	}

	@AfterAll
	public static void tearDown() {
		SecurityContextHolder.clearContext();
		logger.info(new String(new char[25]).replace("\0", "-"));
	}

	@Test
	public void testValidToken() throws ServletException, IOException {

		logger.info("Testing valid token");

		String username = "cliente1";
		LocalDateTime now = LocalDateTime.now();
		Instant issuedAt = now.atZone(ZoneId.systemDefault()).toInstant();
		Instant expiresAt = now.plus(Duration.ofHours(1)).atZone(ZoneId.systemDefault()).toInstant();

		String token = JWT.create()
				.withSubject(username)
				.withIssuedAt(issuedAt)
				.withExpiresAt(expiresAt)
				.sign(Algorithm.HMAC512(SECRET));

		DecodedJWT decodedJWT = JWT.decode(token);
		when(jwtProvider.validateToken("Bearer " + token)).thenReturn(decodedJWT);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Authorization", "Bearer " + token);
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain chain = new MockFilterChain();

		filter.doFilter(request, response, chain);

		assertNotNull(SecurityContextHolder.getContext().getAuthentication());
		assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());

	}

	@Test
	public void testInvalidToken() throws ServletException, IOException {

		logger.info("Testing invalid token");

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Authorization", "Bearer invalid-token");
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain chain = new MockFilterChain();

		when(jwtProvider.validateToken("Bearer invalid-token")).thenReturn(null);

		filter.doFilter(request, response, chain);

		assertNull(SecurityContextHolder.getContext().getAuthentication());
		assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
		assertEquals("Invalid JWT token.", response.getContentAsString());

	}

	@Test
	public void testNoToken() throws ServletException, IOException {

		logger.info("Testing no token");

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain chain = new MockFilterChain();

		filter.doFilter(request, response, chain);

		assertNull(SecurityContextHolder.getContext().getAuthentication());
		assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
		assertEquals("Missing or invalid Authorization header.", response.getContentAsString());

	}

}
