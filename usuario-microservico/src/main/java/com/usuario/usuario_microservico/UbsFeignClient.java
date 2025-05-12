package com.usuario.usuario_microservico;

import com.usuario.usuario_microservico.dto.UbsInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cliente Feign para chamar o micro-serviço de UBS.
 * A URL base vem de application.properties: ubs-service.url=http://localhost:8083
 */
@FeignClient(name = "ubs-client", url = "${ubs-service.url}")
public interface UbsFeignClient {

    /**
     * Busca UBS pelo CNES (String).
     */
    @GetMapping("/ubs/{cnes}")
    UbsInfoDTO getByCnes(@PathVariable("cnes") String cnes);

    /**
     * Busca UBS pelo ID (Long).
     * Você precisa expor no UBS-service um endpoint GET /ubs/id/{id}.
     */
    @GetMapping("/ubs/id/{id}")
    UbsInfoDTO getById(@PathVariable("id") Long id);
}
