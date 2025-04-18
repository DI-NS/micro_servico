package com.medicamento.medicamento_microservico.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;

    private String validToken;
    private String invalidToken;
    private final String secretKey = "super-secure-long-secret-key-for-testing-jwt-token";
    private final SecretKey signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());

    @BeforeEach
    void setUp() {
        tokenService = new TokenService(secretKey);

        // Gerar um token válido para teste
        validToken = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        // Gerar um token inválido (chave incorreta)
        invalidToken = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(Keys.hmacShaKeyFor("wrong-secret-key-for-invalid-token".getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    @Test
    void constructor_ShouldThrowException_WhenSecretKeyIsNullOrBlank() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> new TokenService(null));
        assertEquals("A chave JWT_SECRET não está definida no ambiente. Defina antes de iniciar.", exception.getMessage());

        exception = assertThrows(RuntimeException.class, () -> new TokenService(""));
        assertEquals("A chave JWT_SECRET não está definida no ambiente. Defina antes de iniciar.", exception.getMessage());
    }

    @Test
    void validateToken_ShouldReturnTrue_ForValidToken() {
        assertTrue(tokenService.validateToken(validToken), "Token válido deve ser validado corretamente.");
    }

    @Test
    void validateToken_ShouldReturnFalse_ForInvalidToken() {
        assertFalse(tokenService.validateToken(invalidToken), "Token inválido deve ser rejeitado.");
    }

    @Test
    void validateToken_ShouldReturnFalse_ForExpiredToken() {
        // Criar um token expirado
        String expiredToken = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60)) // 1 hora atrás
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60)) // Expirou há 1 minuto
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        assertFalse(tokenService.validateToken(expiredToken), "Token expirado deve ser rejeitado.");
    }

    @Test
    void getAuthentication_ShouldReturnAuthentication_ForValidToken() {
        Authentication authentication = tokenService.getAuthentication(validToken);

        assertNotNull(authentication, "A autenticação não deve ser nula para um token válido.");
        assertEquals("testuser", authentication.getPrincipal(), "O nome do usuário deve corresponder ao assunto do token.");
        assertTrue(authentication.getAuthorities().isEmpty(), "A lista de autoridades deve estar vazia.");
    }

    @Test
    void getAuthentication_ShouldThrowException_ForInvalidToken() {
        assertThrows(Exception.class, () -> tokenService.getAuthentication(invalidToken), "Deve lançar uma exceção para um token inválido.");
    }
}
