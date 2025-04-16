package Medmap.Ubs_Microservico.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final SecretKey secretKey;
    private final String ctx;   // context‑path (pode ser vazio)

    public JwtAuthenticationFilter(
            @Value("${jwt.secret}") String secret,
            @Value("${server.servlet.context-path:}") String ctx) {

        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.ctx = ctx;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {

        String path = req.getRequestURI().substring(ctx.length()); // remove prefixo

        if (isPublic(req, path)) {
            chain.doFilter(req, res);
            return;
        }

        String header = req.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token ausente");
            return;
        }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(header.substring(7))
                    .getBody();

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(claims.getSubject(), null, null)
            );
            chain.doFilter(req, res);

        } catch (Exception e) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado");
        }
    }

    /* ---------- rotas que NÃO exigem JWT ---------- */
    private boolean isPublic(HttpServletRequest req, String p) {

        /* Swagger / H2 */
        if (p.startsWith("/swagger-ui")  || p.equals("/swagger-ui.html") ||
                p.startsWith("/v3/api-docs") || p.startsWith("/h2-console"))
            return true;

        /* -------- registro e consulta de UBS -------- */
        if ("POST".equalsIgnoreCase(req.getMethod()) && p.equals("/ubs"))
            return true;                                   // cadastro público

        if ("GET".equalsIgnoreCase(req.getMethod()) && p.startsWith("/ubs"))
            return true;                                   // listar / consultar

        /* -------------------------------------------- */
        return false;
    }
}
