package MedMap.controller;

import MedMap.dto.ForgotPasswordRequest;
import MedMap.model.User;
import MedMap.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        String msg = authService.register(user);
        return ResponseEntity.ok(msg);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String cnes,
                                        @RequestParam String password) {
        String token = authService.login(cnes, password);
        return ResponseEntity.ok(token);
    }

    // --- NOVO: esqueci minha senha ---
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest req) {
        String msg = authService.forgotPassword(req);
        return ResponseEntity.ok(msg);
    }
}
