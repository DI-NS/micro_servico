package MedMap.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testa se o contexto de configuração do Spring sobe sem erros.
 */
@SpringBootTest
class ConfigTest {

    @Test
    void contextLoads() {
        assertTrue(true);
    }
}
