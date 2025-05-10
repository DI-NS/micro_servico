package Medmap.Ubs_Microservico.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(
        name          = "auth-client",
        url           = "${auth-service.url}",
        configuration = AuthClientConfig.class
)
public interface AuthClient {

    @PostMapping("/auth/register")
    String registerUbs(@RequestBody Map<String, Object> body);
}
