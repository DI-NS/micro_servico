package Medmap.Ubs_Microservico.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * Comunicação com o Auth‑service (porta 8080).
 */
@FeignClient(name = "auth-client",         // nome interno
        url  = "${auth-service.url}") // lido do application.yml
public interface AuthClient {

    @PostMapping("/auth/register")
    String registerUbs(@RequestBody Map<String, Object> body);
}
