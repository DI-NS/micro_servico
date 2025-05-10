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
    private final String ctx;

    public JwtAuthenticationFilter(
            @Value("${jwt.secret}") String secret,
            @Value("${server.servlet.context-path:}") String ctx
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.ctx = ctx;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {

        String path = req.getRequestURI().substring(ctx.length());
        if (isPublic(req, path)) {
            chain.doFilter(req, res);
            return;
        }

        String header = req.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("{\"erro\":\"Token ausente\"}");
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
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("{\"erro\":\"Token inválido ou expirado\"}");
        }
    }

    private boolean isPublic(HttpServletRequest req, String p) {
        if (p.startsWith("/swagger-ui") || p.equals("/swagger-ui.html") ||
                p.startsWith("/v3/api-docs")  || p.startsWith("/h2-console"))
            return true;
        if ("POST".equalsIgnoreCase(req.getMethod()) && p.equals("/ubs"))
            return true;
        if ("GET".equalsIgnoreCase(req.getMethod()) && p.startsWith("/ubs"))
            return true;
        return false;
    }
}
