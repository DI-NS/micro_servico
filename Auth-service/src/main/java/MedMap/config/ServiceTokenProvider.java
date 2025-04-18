package MedMap.config;

import org.springframework.stereotype.Component;
import MedMap.service.TokenService;

@Component
public class ServiceTokenProvider {

    private final TokenService tokenService;

    public ServiceTokenProvider(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Retorna um JWT válido para uso interno por outros microsserviços.
     */
    public String getServiceToken() {
        return tokenService.generateServiceToken();
    }
}
