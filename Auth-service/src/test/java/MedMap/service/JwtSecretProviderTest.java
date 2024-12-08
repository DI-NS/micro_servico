package MedMap.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;

class JwtSecretProviderTest {

    @Test
    @DisplayName("Deve retornar o segredo JWT corretamente")
    void testGetJwtSecret() {
        // Arrange
        String secret = "XPdF3BA71M1oV1ZkkHXVkX4FZSeC1lwX4Ltnv2HmHHA=";

        // Act
        JwtSecretProvider jwtSecretProvider = new JwtSecretProvider(secret);
        String returnedSecret = jwtSecretProvider.getJwtSecret();

        // Assert
        Assertions.assertEquals(secret, returnedSecret, "O segredo JWT retornado não corresponde ao esperado.");
    }

    @Test
    @DisplayName("Deve lançar exceção se o segredo JWT for nulo")
    void testConstructorThrowsExceptionWhenSecretIsNull() {
        // Arrange & Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new JwtSecretProvider(null);
        }, "Deveria lançar uma exceção quando o segredo JWT for nulo.");
    }

    @Test
    @DisplayName("Deve lançar exceção se o segredo JWT for vazio")
    void testConstructorThrowsExceptionWhenSecretIsBlank() {
        // Arrange & Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new JwtSecretProvider(" ");
        }, "Deveria lançar uma exceção quando o segredo JWT for vazio.");
    }
}
