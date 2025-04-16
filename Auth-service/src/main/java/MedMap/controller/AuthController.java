package MedMap.controller;

import MedMap.model.User;
import MedMap.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints de autenticação.
 *  • POST /auth/register  – uso interno pelo UBS‑service
 *  • POST /auth/login     – login da UBS para obter JWT
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /* --------- uso interno --------- */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        String msg = authService.register(user);
        return ResponseEntity.ok(msg);
    }

    /* --------- login público -------- */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String cnes,
                                        @RequestParam String password) {
        String token = authService.login(cnes, password);
        return ResponseEntity.ok(token);
    }
}
