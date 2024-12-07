package MedMap.controller;

import MedMap.model.User;
import MedMap.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testa o AuthController, verificando rotas de registro e login.
 */
class AuthControllerTest {

    private AuthController authController;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = Mockito.mock(AuthService.class);
        authController = new AuthController(authService);
    }

    @Test
    void register_ShouldReturnSuccessMessage() {
        Mockito.when(authService.register(any(User.class))).thenReturn("UBS registrada com sucesso!");
        ResponseEntity<String> response = authController.register(new User());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("UBS registrada com sucesso!", response.getBody());
    }

    @Test
    void login_ShouldReturnToken() {
        Mockito.when(authService.login("Test UBS", "123456")).thenReturn("mock-token");
        ResponseEntity<String> response = authController.login("Test UBS", "123456");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("mock-token", response.getBody());
    }
}
