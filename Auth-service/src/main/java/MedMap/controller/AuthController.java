package MedMap.controller;

import MedMap.model.User;
import MedMap.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador responsável pelas rotas de autenticação.
 * - Registro: Recebe nomeUbs, cnes, address e password.
 * - Login: Recebe cnes e password.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint para registrar uma nova UBS.
     *
     * @param user Dados da UBS (nomeUbs, cnes, address, password).
     * @return Mensagem de sucesso.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        String message = authService.register(user);
        return ResponseEntity.ok(message);
    }

    /**
     * Endpoint para autenticar uma UBS pelo CNES e senha.
     *
     * @param cnes     Código CNES da UBS.
     * @param password Senha da UBS.
     * @return Token JWT se a autenticação for bem-sucedida.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String cnes, @RequestParam String password) {
        String token = authService.login(cnes, password);
        return ResponseEntity.ok(token);
    }
}
