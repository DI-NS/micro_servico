package MedMap.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

/**
 * Filtro responsável por validar o token JWT em requisições protegidas.
 *
 * Caso o endpoint seja público (como /auth/**, /swagger-ui/**, /v3/api-docs/**, /h2-console/**),
 * este filtro não deve exigir autenticação, permitindo que a requisição chegue ao controlador.
 *
 * Caso contrário, verifica se há um cabeçalho Authorization com "Bearer <token>".
 * - Se ausente ou inválido, retorna 401 "Acesso não autorizado".
 * - Se o token existir e for inválido/expirado, retorna 401 "Token JWT inválido ou expirado".
 * - Se o token for válido, autentica o contexto de segurança.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtSecretProvider jwtSecretProvider;

    public JwtAuthenticationFilter(JwtSecretProvider jwtSecretProvider) {
        this.jwtSecretProvider = jwtSecretProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Use getRequestURI() para garantir que teremos o caminho completo da requisição.
        String path = request.getRequestURI();

        // Se for uma rota pública, não exige token e não altera a resposta
        if (isPublicEndpoint(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        // Verifica se o header existe e começa com "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Acesso não autorizado");
            return;
        }

        String jwt = authHeader.substring(7);
        try {
            String jwtSecret = jwtSecretProvider.getJwtSecret();
            byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            String cnes = claims.getSubject();

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(cnes, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token JWT inválido ou expirado");
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Determina se o endpoint é público, ou seja, não requer autenticação JWT.
     * Aqui listamos os padrões de URL que devem ser acessíveis sem token.
     */
    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/auth/") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/h2-console");
    }
}
