package com.usuario.usuario_microservico;

import com.usuario.usuario_microservico.dto.UbsInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cliente Feign para chamar o micro-serviço de UBS.
 * A URL base vem de application.properties: ubs-service.url=http://localhost:8083
 */
@FeignClient(name = "ubs-client",
        url  = "${ubs-service.url}",
        path = "/ubs")      // aqui já puxa o /ubs
public interface UbsFeignClient {

    @GetMapping("/{cnes}")        // → https://…/ubs/{cnes}
    UbsInfoDTO getByCnes(@PathVariable String cnes);

    @GetMapping("/id/{id}")       // → https://…/ubs/id/{id}
    UbsInfoDTO getById(@PathVariable Long id);
}

