package MedMap.service;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class JwtSecretProvider {

    private final String jwtSecret;

    public JwtSecretProvider(@Value("${jwt.secret}") String jwtSecretEnv) {
        if (jwtSecretEnv == null || jwtSecretEnv.isBlank()) {
            throw new IllegalArgumentException("A variável de ambiente 'JWT_SECRET' deve ser definida.");
        }
        this.jwtSecret = jwtSecretEnv;
    }

    /**
     * Retorna o segredo JWT.
     *
     * @return Segredo codificado em Base64.
     */
    public String getJwtSecret() {
        return jwtSecret;
    }
}
