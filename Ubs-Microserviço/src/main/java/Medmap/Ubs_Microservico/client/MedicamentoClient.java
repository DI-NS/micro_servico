package Medmap.Ubs_Microservico.client;

import Medmap.Ubs_Microservico.dto.MedicamentoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Comunicação com Medicamento-service
 */
@FeignClient(name = "medicamento-client",
        url = "${medicamento-service.url}")
public interface MedicamentoClient {

    /**
     * GET /medicamento?ubs={cnes}
     */
    @GetMapping(value = "/medicamento", params = "ubs")
    List<MedicamentoDTO> listByUbs(@RequestParam("ubs") String cnes);
}
