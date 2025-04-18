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

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtSecretProvider jwtSecretProvider;

    public JwtAuthenticationFilter(JwtSecretProvider jwtSecretProvider) {
        this.jwtSecretProvider = jwtSecretProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // libera apenas login, swagger e h2
        if (isPublicEndpoint(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // obtém header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"erro\":\"Token de autenticação ausente ou inválido.\"}"
            );
            return;
        }

        String jwt = authHeader.substring(7);
        try {
            // carrega secret direto em bytes UTF-8
            String secret = jwtSecretProvider.getJwtSecret();
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            String sub       = claims.getSubject();
            Boolean isService = claims.get("service", Boolean.class);

            // se é chamada a /auth/register, exige token de serviço
            if (path.equals("/auth/register")) {
                if (!"ubs-service".equals(sub) || !Boolean.TRUE.equals(isService)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write(
                            "{\"erro\":\"Token de serviço inválido para registro.\"}"
                    );
                    return;
                }
                // tudo ok: libera registro
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken("ubs-service", null, null)
                );
            } else {
                // para outros endpoints (por ex. login, depois de públicos),
                // aceita qualquer token válido
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(sub, null, null)
                );
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"erro\":\"Token JWT inválido ou expirado.\"}"
            );
        }
    }

    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/auth/login")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/h2-console");
    }
}
