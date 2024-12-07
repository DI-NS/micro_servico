package MedMap.service;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

/**
 * Fornecedor do segredo JWT. Se não for fornecido via configuração, gera um segredo novo.
 */
@Component
public class JwtSecretProvider {

    private final String jwtSecret;

    public JwtSecretProvider(@Value("${jwt.secret:}") String jwtSecretEnv) {
        if (jwtSecretEnv == null || jwtSecretEnv.isBlank()) {
            // Gera uma nova chave se não estiver configurada
            this.jwtSecret = Base64.getEncoder()
                    .encodeToString(Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512).getEncoded());
        } else {
            // Usa a chave definida nas configurações
            this.jwtSecret = jwtSecretEnv;
        }
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
