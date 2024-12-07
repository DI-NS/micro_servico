package com.usuario.usuario_microservico.Service;

import com.usuario.usuario_microservico.MedicamentoFeignClient;
import com.usuario.usuario_microservico.Model.Medicamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private MedicamentoFeignClient feignClient;

    //Método para buscar todos os medicamentos disponíveis através do Feign Client.
    public List<Medicamento> getAllMedicamentos() {
        // Chama o Feign Client para obter a lista de medicamentos do microserviço.
        return feignClient.getAllMedicamentos();
    }

    //Método para buscar um medicamento específico pelo ID através do Feign Client.
    public Medicamento getMedicamentoById(Long id) {
        // Chama o Feign Client para obter o medicamento pelo ID do microserviço.
        return feignClient.getMedicamentoById(id);
    }
}
