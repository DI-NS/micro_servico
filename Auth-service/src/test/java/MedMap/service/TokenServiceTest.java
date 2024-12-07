package MedMap.service;

import MedMap.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa o TokenService, garantindo que o token inclua o CNES e o nome da UBS.
 */
@SpringBootTest
class TokenServiceTest {

    @Autowired
    TokenService tokenService;

    @Autowired
    JwtSecretProvider jwtSecretProvider;

    @Test
    void generateTokenShouldContainCnesAndNomeUbs() {
        User user = new User("UBS Teste", "654321", "Rua ABC", "senha123");
        user.setHashedPassword("hashedSenha");
        String token = tokenService.generateToken(user.getCnes());

        assertNotNull(token);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecretProvider.getJwtSecret())))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("654321", claims.getSubject());
        assertEquals("UBS Teste", claims.get("nomeUbs"));
    }
}
