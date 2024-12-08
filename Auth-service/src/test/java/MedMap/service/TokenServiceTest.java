package MedMap.service;

import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

class TokenServiceTest {

    private TokenService tokenService;
    private final String secretKey = "XPdF3BA71M1oV1ZkkHXVkX4FZSeC1lwX4Ltnv2HmHHA=";
    private final long expiration = 3600; // 1 hora em segundos

    @BeforeEach
    void setUp() {
        tokenService = new TokenService(secretKey, expiration);
    }

    @Test
    @DisplayName("Deve gerar um token válido")
    void testGenerateToken() {
        // Arrange
        String cnes = "123456";

        // Act
        String token = tokenService.generateToken(cnes);

        // Assert
        Assertions.assertNotNull(token, "O token gerado não deve ser nulo.");
        Assertions.assertTrue(tokenService.validateToken(token), "O token gerado deve ser válido.");
    }

    @Test
    @DisplayName("Deve validar um token válido")
    void testValidateToken() {
        // Arrange
        String cnes = "123456";
        String token = tokenService.generateToken(cnes);

        // Act
        boolean isValid = tokenService.validateToken(token);

        // Assert
        Assertions.assertTrue(isValid, "O token deve ser considerado válido.");
    }

    @Test
    @DisplayName("Deve invalidar um token inválido")
    void testValidateInvalidToken() {
        // Arrange
        String invalidToken = "invalidToken";

        // Act
        boolean isValid = tokenService.validateToken(invalidToken);

        // Assert
        Assertions.assertFalse(isValid, "Um token inválido não deve ser considerado válido.");
    }

    @Test
    @DisplayName("Deve retornar o assunto correto de um token válido")
    void testGetSubjectFromToken() {
        // Arrange
        String cnes = "123456";
        String token = tokenService.generateToken(cnes);

        // Act
        String subject = tokenService.getSubjectFromToken(token);

        // Assert
        Assertions.assertEquals(cnes, subject, "O assunto retornado deve corresponder ao CNES fornecido.");
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar TokenService com chave secreta inválida")
    void testConstructorThrowsExceptionForInvalidSecretKey() {
        // Arrange & Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new TokenService(null, expiration);
        }, "Deveria lançar uma exceção para chave secreta nula.");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new TokenService(" ", expiration);
        }, "Deveria lançar uma exceção para chave secreta em branco.");
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar TokenService com tempo de expiração inválido")
    void testConstructorWithInvalidExpiration() {
        // Arrange & Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new TokenService(secretKey, -1);
        }, "Deveria lançar uma exceção para tempo de expiração negativo.");
    }
}
