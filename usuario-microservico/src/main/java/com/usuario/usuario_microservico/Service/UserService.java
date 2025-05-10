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

    public List<Medicamento> getAllMedicamentos() {
        return feignClient.getAllMedicamentos();
    }

    public Medicamento getMedicamentoById(Long id) {
        return feignClient.getMedicamentoById(id);
    }
}
