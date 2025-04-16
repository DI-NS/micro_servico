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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final byte[] keyBytes;
    private final String ctx;   // context‑path (pode ser vazio)

    public JwtAuthenticationFilter(
            @Value("${jwt.secret}") String secret,
            @Value("${server.servlet.context-path:}") String ctx) {

        this.keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.ctx = ctx;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {

        String path = req.getRequestURI().substring(ctx.length()); // remove prefixo

        if (isPublic(path)) {
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
                    .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
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

    /* rotas que não exigem JWT – sem context‑path */
    private boolean isPublic(String p) {
        return p.startsWith("/swagger-ui")     ||
                p.equals("/swagger-ui.html")    ||
                p.startsWith("/v3/api-docs")    ||
                p.startsWith("/api-docs")       ||
                p.startsWith("/swagger-config") ||
                p.startsWith("/h2-console")     ||
                p.startsWith("/auth/");
    }
}
