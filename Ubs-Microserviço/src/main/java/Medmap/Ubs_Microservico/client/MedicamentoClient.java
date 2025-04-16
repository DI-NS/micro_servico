package Medmap.Ubs_Microservico.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import Medmap.Ubs_Microservico.dto.MedicamentoDTO;
import java.util.List;

/** Comunicação com Medicamento‑service */
@FeignClient(name = "medicamento-client",
        url = "${medicamento-service.url}")
public interface MedicamentoClient {

    @GetMapping("/medicamento?ubs={cnes}")
    List<MedicamentoDTO> listByUbs(@PathVariable("cnes") String cnes);
}