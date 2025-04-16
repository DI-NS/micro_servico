package Medmap.Ubs_Microservico.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI api() {

        /* --------- definição do esquema Bearer --------- */
        SecurityScheme bearerScheme = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");     // só exibição

        /* --------- requisito global (todas as rotas) ---- */
        SecurityRequirement requirement = new SecurityRequirement()
                .addList(SECURITY_SCHEME_NAME);

        return new OpenAPI()
                .info(new Info()
                        .title("UBS‑Service API")
                        .version("1.0")
                        .description("Gerenciamento de UBS"))
                .addSecurityItem(requirement)               // ← aplica globalmente
                .schemaRequirement(SECURITY_SCHEME_NAME, bearerScheme);
    }
}
