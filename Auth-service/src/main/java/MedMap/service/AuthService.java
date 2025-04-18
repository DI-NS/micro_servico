package MedMap.service;

import MedMap.exception.InvalidCredentialsException;
import MedMap.exception.UserAlreadyExistsException;
import MedMap.model.User;
import MedMap.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       TokenService tokenService) {
        this.userRepository   = userRepository;
        this.passwordEncoder  = passwordEncoder;
        this.tokenService     = tokenService;
    }

    public String register(User user) {
        if (userRepository.findByCnes(user.getCnes()).isPresent()) {
            throw new UserAlreadyExistsException("Uma UBS com esse CNES já está registrada.");
        }
        user.setHashedPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "UBS registrada com sucesso!";
    }

    public String login(String cnes, String password) {
        User user = userRepository.findByCnes(cnes)
                .orElseThrow(() -> new InvalidCredentialsException("UBS não encontrada ou dados inválidos"));

        if (!passwordEncoder.matches(password, user.getHashedPassword())) {
            throw new InvalidCredentialsException("CNES ou senha inválidos");
        }

        // Aqui passamos o CNES e o ID do usuário (que será o ubsId no token)
        return tokenService.generateToken(user.getCnes(), user.getId());
    }
}
