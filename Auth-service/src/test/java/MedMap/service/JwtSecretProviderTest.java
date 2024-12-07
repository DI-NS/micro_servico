package MedMap.service;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa o JwtSecretProvider garantindo que o segredo seja gerado adequadamente
 * quando não fornecido, e usado corretamente quando fornecido.
 */
class JwtSecretProviderTest {

    @Test
    void shouldUseProvidedSecret() {
        String providedSecret = "myProvidedSecret";
        String encoded = Base64.getEncoder().encodeToString(providedSecret.getBytes());
        JwtSecretProvider provider = new JwtSecretProvider(encoded);
        String secret = provider.getJwtSecret();
        assertNotNull(secret);
        assertEquals(encoded, secret);
    }

    @Test
    void shouldGenerateNewSecretIfNullProvided() {
        JwtSecretProvider provider = new JwtSecretProvider(null);
        String secret = provider.getJwtSecret();
        assertNotNull(secret);
        assertFalse(secret.isBlank());
    }

    @Test
    void shouldGenerateNewSecretIfEmptyProvided() {
        JwtSecretProvider provider = new JwtSecretProvider("");
        String secret = provider.getJwtSecret();
        assertNotNull(secret);
        assertFalse(secret.isBlank());
    }

    @Test
    void shouldGenerateNewSecretIfBlankProvided() {
        JwtSecretProvider provider = new JwtSecretProvider("   ");
        String secret = provider.getJwtSecret();
        assertNotNull(secret);
        assertFalse(secret.isBlank());
    }
}
