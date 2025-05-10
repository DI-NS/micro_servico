package com.usuario.usuario_microservico.Controller;

import com.usuario.usuario_microservico.Model.Medicamento;
import com.usuario.usuario_microservico.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/medicamentos")
    public List<Medicamento> getAllMedicamentos() {
        return userService.getAllMedicamentos();
    }

    @GetMapping("/medicamentos/{id}")
    public Medicamento getMedicamentoById(@PathVariable Long id) {
        return userService.getMedicamentoById(id);
    }
}
