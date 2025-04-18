package Medmap.Ubs_Microservico.client;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import Medmap.Ubs_Microservico.config.ServiceTokenProvider;

@Configuration
public class AuthClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor(ServiceTokenProvider tokenProvider) {
        return template ->
                template.header("Authorization", "Bearer " + tokenProvider.getServiceToken());
    }
}
