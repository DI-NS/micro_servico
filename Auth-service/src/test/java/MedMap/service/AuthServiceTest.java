package MedMap.service;

import MedMap.exception.InvalidCredentialsException;
import MedMap.exception.UserAlreadyExistsException;
import MedMap.model.User;
import MedMap.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testa o AuthService, garantindo que registro e login funcionem adequadamente.
 */
class AuthServiceTest {

    private AuthService authService;
    private UserRepository userRepository;
    private TokenService tokenService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        tokenService = Mockito.mock(TokenService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);

        Mockito.when(passwordEncoder.encode(any())).thenReturn("hashed-password");
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(true);

        authService = new AuthService(userRepository, passwordEncoder, tokenService);
    }

    @Test
    void register_ShouldRegisterNewUser() {
        User user = new User();
        user.setNomeUbs("Test UBS");
        user.setCnes("123456");

        Mockito.when(userRepository.findByCnes("123456"))
                .thenReturn(Optional.empty());

        String result = authService.register(user);
        assertEquals("UBS registrada com sucesso!", result);
        Mockito.verify(userRepository).save(any(User.class));
    }

    @Test
    void register_ShouldThrowUserAlreadyExistsException() {
        User user = new User();
        user.setNomeUbs("Test UBS");
        user.setCnes("123456");

        Mockito.when(userRepository.findByCnes("123456"))
                .thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(user));
    }

    @Test
    void login_ShouldReturnToken() {
        User user = new User();
        user.setNomeUbs("Test UBS");
        user.setCnes("123456");
        user.setHashedPassword("hashed-password");

        Mockito.when(userRepository.findByCnes("123456"))
                .thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches("123456", "hashed-password"))
                .thenReturn(true);
        Mockito.when(tokenService.generateToken(user.getCnes())
                )
                .thenReturn("mock-token");

        String token = authService.login("123456", "123456");
        assertEquals("mock-token", token);
    }

    @Test
    void login_ShouldThrowInvalidCredentialsExceptionWhenNotFound() {
        Mockito.when(userRepository.findByCnes("123456"))
                .thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authService.login("123456", "123456"));
    }

    @Test
    void login_ShouldThrowInvalidCredentialsExceptionWhenPasswordDoesNotMatch() {
        User user = new User();
        user.setCnes("123456");
        user.setHashedPassword("hashed-password");

        Mockito.when(userRepository.findByCnes("123456"))
                .thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches("wrongpass", "hashed-password")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login("123456", "wrongpass"));
    }
}
