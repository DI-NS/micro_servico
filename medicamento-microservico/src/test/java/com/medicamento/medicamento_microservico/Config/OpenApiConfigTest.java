package com.medicamento.medicamento_microservico.Config;

import com.medicamento.medicamento_microservico.config.OpenApiConfig;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpenApiConfigTest {

    @Test
    void testCustomOpenAPI() {
        // Arrange
        OpenApiConfig openApiConfig = new OpenApiConfig();

        // Act
        OpenAPI openAPI = openApiConfig.customOpenAPI();

        // Assert
        assertNotNull(openAPI, "A configuração do OpenAPI não deve ser nula.");
        assertNotNull(openAPI.getComponents(), "Os componentes do OpenAPI não devem ser nulos.");
        assertNotNull(openAPI.getComponents().getSecuritySchemes(), "Os esquemas de segurança não devem ser nulos.");
        assertTrue(openAPI.getComponents().getSecuritySchemes().containsKey("bearerAuth"),
                "Deve haver um esquema de segurança chamado 'bearerAuth'.");

        SecurityScheme securityScheme = openAPI.getComponents().getSecuritySchemes().get("bearerAuth");
        assertNotNull(securityScheme, "O esquema de segurança 'bearerAuth' não deve ser nulo.");
        assertTrue(securityScheme.getType().equals(SecurityScheme.Type.HTTP),
                "O tipo do esquema de segurança deve ser HTTP.");
        assertTrue("bearer".equals(securityScheme.getScheme()), "O esquema deve ser 'bearer'.");
        assertTrue("JWT".equals(securityScheme.getBearerFormat()), "O formato do bearer deve ser 'JWT'.");

        assertNotNull(openAPI.getSecurity(), "Os requisitos de segurança não devem ser nulos.");
        assertTrue(openAPI.getSecurity().stream()
                        .anyMatch(req -> req.containsKey("bearerAuth")),
                "Os requisitos de segurança devem conter o esquema 'bearerAuth'.");
    }
}
