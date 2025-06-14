// src/main/java/Medmap/Ubs_Microservico/client/AuthClient.java
package Medmap.Ubs_Microservico.client;

import Medmap.Ubs_Microservico.dto.ForgotPasswordRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "auth-client", url = "${auth-service.url}")
public interface AuthClient {

    @PostMapping("/auth/register")
    void registerUbs(@RequestBody Map<String, Object> body);

    // --- NOVO: chama o endpoint de reset de senha no Auth-service ---
    @PostMapping("/auth/forgot-password")
    void forgotPassword(@RequestBody ForgotPasswordRequestDto dto);
}
