package com.usuario.usuario_microservico;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.usuario.usuario_microservico.Model.Medicamento;
import java.util.List;

/**
 * Interface Feign Client para comunicação com o microserviço de Medicamentos.
 * Esta interface é responsável por abstrair as chamadas HTTP para o serviço de medicamentos.
 */
@FeignClient(name = "medicamento-microservico", url = "http://localhost:8080/medicamento")
public interface MedicamentoFeignClient {

    //Método para buscar todos os medicamentos disponíveis no microserviço de medicamentos.
    @GetMapping
    List<Medicamento> getAllMedicamentos();

    //Método para buscar um medicamento específico pelo seu ID.
    @GetMapping("/{id}")
    Medicamento getMedicamentoById(@PathVariable Long id);
}
