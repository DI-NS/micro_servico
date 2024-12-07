package MedMap.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configurações da documentação da API usando OpenAPI/Swagger.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Cria a configuração básica da documentação da API.
     *
     * @return Objeto OpenAPI configurado com informações da API.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MedMap API")
                        .version("1.0")
                        .description("Documentação da API MedMap"));
    }
}
