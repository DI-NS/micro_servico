package MedMap.service;

import MedMap.exception.InvalidCredentialsException;
import MedMap.exception.UserAlreadyExistsException;
import MedMap.model.User;
import MedMap.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pela lógica de autenticação e registro.
 * Aplica princípios SOLID, mantendo apenas a lógica de autenticação.
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
     * Registra uma nova UBS no sistema.
     * Recebe nomeUbs, cnes, address e password.
     * O cnes é armazenado em texto puro (identificador público).
     * A password é hasheada antes de salvar.
     *
     * @param user Dados da UBS a ser registrada.
     * @return Mensagem de sucesso.
     */
    public String register(User user) {
        // Verifica se já existe uma UBS com o mesmo CNES
        if (userRepository.findByCnes(user.getCnes()).isPresent()) {
            throw new UserAlreadyExistsException("Uma UBS com esse CNES já está registrada.");
        }

        // Hasheia a senha do usuário
        user.setHashedPassword(passwordEncoder.encode(user.getPassword()));

        // Salva o usuário no banco
        userRepository.save(user);
        return "UBS registrada com sucesso!";
    }

    /**
     * Autentica uma UBS com base no CNES e senha.
     *
     * @param cnes    Código CNES da UBS (identificador).
     * @param password Senha fornecida pela UBS.
     * @return Token JWT para acesso às rotas protegidas.
     */
    public String login(String cnes, String password) {
        User user = userRepository.findByCnes(cnes)
                .orElseThrow(() -> new InvalidCredentialsException("UBS não encontrada ou dados inválidos"));

        // Verifica se a senha fornecida corresponde ao hash armazenado
        if (!passwordEncoder.matches(password, user.getHashedPassword())) {
            throw new InvalidCredentialsException("CNES ou senha inválidos");
        }

        // Gera o token JWT contendo nomeUbs e cnes
        return tokenService.generateToken(user.getCnes());
    }
}
