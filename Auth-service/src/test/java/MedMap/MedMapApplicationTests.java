package MedMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE, // Garante que nenhum servidor será iniciado
        properties = {
                "spring.main.web-application-type=none", // Desativa o servidor web
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration", // Desativa segurança
                "jwt.secret=super-secure-long-secret-key-for-jwt" // Configuração para JWT
        }
)
class MedMapApplicationTests {

    @Autowired
    private ApplicationContext context;

    // Mocks para dependências desnecessárias
    @MockBean
    private MedMap.service.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private MedMap.service.JwtSecretProvider jwtSecretProvider;

    @MockBean
    private MedMap.service.TokenService tokenService;

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:testdb");
        registry.add("spring.datasource.driver-class-name", () -> "org.h2.Driver");
        registry.add("spring.datasource.username", () -> "sa");
        registry.add("spring.datasource.password", () -> "");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.H2Dialect");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Test
    void contextLoads() {
        assertNotNull(context, "O contexto não foi carregado.");
    }


}
