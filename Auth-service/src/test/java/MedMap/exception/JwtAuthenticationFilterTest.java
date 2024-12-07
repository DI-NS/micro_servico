package MedMap.exception;

import MedMap.model.User;
import MedMap.service.JwtSecretProvider;
import MedMap.service.TokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;
import java.util.Date;

import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testa o JwtAuthenticationFilter sem endpoints protegidos reais.
 * - Endpoints públicos: /auth/**, /swagger-ui/**, /v3/api-docs/**, /h2-console
 *   Verificamos que não retornam 401 por causa do filtro.
 * - Endpoint inexistente não público (ex: /private/data): Deve exigir token.
 *
 * Observação: Para o teste do /auth/login, primeiro criamos um usuário, para que o login não falhe por lógica de negócio.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class JwtAuthenticationFilterTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TokenService tokenService;

    @Autowired
    JwtSecretProvider jwtSecretProvider;

    /**
     * Primeiro criamos um usuário para garantir que o login não retorne 401 por credenciais inválidas,
     * assim, se o login falhar com 401, seria culpa do filtro. Caso contrário, deve retornar outro status (não 401).
     */
    @Test
    void publicEndpointAuthLoginShouldNotRequireToken() throws Exception {
        // Cria usuário antes de logar
        String jsonUser = "{\"nomeUbs\":\"UBS Teste\",\"cnes\":\"123456\",\"address\":\"Rua Teste\",\"password\":\"senha\"}";
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().is(not(401))); // Registro bem-sucedido (não deve ser 401)

        // Agora faz login com o usuário recém-criado
        mockMvc.perform(post("/auth/login")
                        .param("cnes", "123456")
                        .param("password", "senha"))
                .andExpect(status().is(not(401))); // Deve passar pelo filtro sem exigir token e não retornar 401
    }

    @Test
    void publicEndpointAuthRegisterShouldNotRequireToken() throws Exception {
        String cnesUnico = String.valueOf(System.currentTimeMillis());
        String jsonUser = "{\"nomeUbs\":\"UBS Teste\", \"cnes\":\""+ cnesUnico +"\", \"address\":\"Rua Teste\", \"password\":\"senha\"}";
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().is(not(401)));
    }

    @Test
    void publicEndpointSwaggerShouldNotRequireToken() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().is(not(401)));
    }

    @Test
    void publicEndpointApiDocsShouldNotRequireToken() throws Exception {
        mockMvc.perform(get("/v3/api-docs/something"))
                .andExpect(status().is(not(401)));
    }

    @Test
    void publicEndpointH2ConsoleShouldNotRequireToken() throws Exception {
        mockMvc.perform(get("/h2-console"))
                .andExpect(status().is(not(401)));
    }

    @Test
    void shouldReturnUnauthorizedWhenNoAuthorizationHeaderOnNonPublicEndpoint() throws Exception {
        mockMvc.perform(get("/private/data"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Acesso não autorizado"));
    }

    @Test
    void shouldReturnUnauthorizedWhenAuthorizationHeaderNotBearerOnNonPublicEndpoint() throws Exception {
        mockMvc.perform(get("/private/data")
                        .header("Authorization", "Basic YWxhZGRpbjpvcGVuc2VzYW1l"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Acesso não autorizado"));
    }

    @Test
    void shouldReturnUnauthorizedForInvalidTokenOnNonPublicEndpoint() throws Exception {
        mockMvc.perform(get("/private/data")
                        .header("Authorization", "Bearer invalid_token"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token JWT inválido ou expirado"));
    }

    @Test
    void shouldReturnUnauthorizedForEmptyBearerOnNonPublicEndpoint() throws Exception {
        mockMvc.perform(get("/private/data")
                        .header("Authorization", "Bearer "))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token JWT inválido ou expirado"));
    }

    @Test
    void shouldReturnUnauthorizedForExpiredTokenOnNonPublicEndpoint() throws Exception {
        String expiredToken = generateExpiredToken();
        mockMvc.perform(get("/private/data")
                        .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token JWT inválido ou expirado"));
    }

    @Test
    void shouldNotReturn401WithValidTokenOnNonPublicEndpoint() throws Exception {
        // Token válido, endpoint inexistente não público deve ser filtrado, mas não retornar 401 por causa do token.
        User user = new User("UBS Teste", "111111", "Endereço", "senha");
        user.setHashedPassword("hash");
        String token = tokenService.generateToken(user.getCnes());
        assertNotNull(token);

        mockMvc.perform(get("/private/data")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is(not(401))); // provavelmente 404, mas não 401 do filtro
    }

    private String generateExpiredToken() {
        String jwtSecret = jwtSecretProvider.getJwtSecret();
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        var secretKey = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setSubject("123456")
                .setIssuedAt(new Date(System.currentTimeMillis() - 60000))
                .setExpiration(new Date(System.currentTimeMillis() - 30000))
                .signWith(secretKey)
                .compact();
    }
}
