package MedMap.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa a configuração da documentação OpenAPI.
 */
class OpenApiConfigTest {

    @Test
    void testCustomOpenAPI() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI api = config.customOpenAPI();
        assertNotNull(api.getInfo());
        assertEquals("Documentação da API MedMap", api.getInfo().getDescription());
    }
}
