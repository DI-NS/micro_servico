package MedMap.service;

import MedMap.exception.InvalidCredentialsException;
import MedMap.exception.UserAlreadyExistsException;
import MedMap.model.User;
import MedMap.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pela lógica de autenticação e registro.
 * Neste modelo, o Auth‑service registra os dados (usuário) recebidos e somente autentica as UBS.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    /**
     * Registra uma nova UBS no Auth‑service.
     * Recebe nomeUbs, cnes e password.
     *
     * @param user Dados da UBS a ser registrada.
     * @return Mensagem de sucesso.
     */
    public String register(User user) {
        // Verifica se já existe uma UBS com o mesmo CNES
        if (userRepository.findByCnes(user.getCnes()).isPresent()) {
            throw new UserAlreadyExistsException("Uma UBS com esse CNES já está registrada.");
        }

        // Hasheia a senha e persiste o usuário
        user.setHashedPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "UBS registrada com sucesso!";
    }

    /**
     * Autentica uma UBS com base no CNES e senha.
     *
     * @param cnes     Código CNES da UBS.
     * @param password Senha fornecida pela UBS.
     * @return Token JWT se a autenticação for bem-sucedida.
     */
    public String login(String cnes, String password) {
        User user = userRepository.findByCnes(cnes)
                .orElseThrow(() -> new InvalidCredentialsException("UBS não encontrada ou dados inválidos"));

        if (!passwordEncoder.matches(password, user.getHashedPassword())) {
            throw new InvalidCredentialsException("CNES ou senha inválidos");
        }

        return tokenService.generateToken(user.getCnes());
    }
}
