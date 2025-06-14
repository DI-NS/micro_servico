package com.usuario.usuario_microservico;

import com.usuario.usuario_microservico.Model.Medicamento;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "medicamento-client",
        url  = "${medicamento.service.url}",
        path = "/medicamento")
public interface MedicamentoFeignClient {
    @GetMapping        List<Medicamento> getAllMedicamentos();
    @GetMapping("/{id}") Medicamento       getMedicamentoById(@PathVariable Long id);
}
