package com.usuario.usuario_microservico.Controller;

import com.usuario.usuario_microservico.Model.Medicamento;
import com.usuario.usuario_microservico.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    //Endpoint para buscar todos os medicamentos disponíveis.
    @GetMapping("/medicamentos")
    public List<Medicamento> getAllMedicamentos() {
        // Chama o serviço para buscar a lista de medicamentos e retorna o resultado.
        return userService.getAllMedicamentos();
    }

    //Endpoint para buscar um medicamento específico pelo seu ID.
    @GetMapping("/medicamentos/{id}")
    public Medicamento getMedicamentoById(@PathVariable Long id) {
        // Chama o serviço para buscar o medicamento pelo ID e retorna o resultado.
        return userService.getMedicamentoById(id);
    }
}
