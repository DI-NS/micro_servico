package Medmap.Ubs_Microservico.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final byte[] keyBytes;
    private final String ctx;   // context‑path (pode ser vazio)

    public JwtAuthenticationFilter(
            @Value("${jwt.secret}") String secret,
            @Value("${server.servlet.context-path:}") String ctx) {

        this.keyBytes = Base64.getDecoder().decode(secret);
        this.ctx = ctx;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {

        String path = req.getRequestURI().substring(ctx.length()); // remove prefixo

        // ---------- rotas que NÃO exigem token ----------
        if (isPublic(path, req.getMethod())) {
            chain.doFilter(req, res);
            return;
        }

        // ---------- verifica header Authorization ----------
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

    /* ----------------------------------------------------
       Rotas públicas  (NÃO exigem JWT)
       ---------------------------------------------------- */
    private boolean isPublic(String p, String method) {

        // Swagger / H2
        if (p.startsWith("/swagger-ui")     || p.equals("/swagger-ui.html") ||
                p.startsWith("/v3/api-docs")    || p.startsWith("/api-docs")    ||
                p.startsWith("/swagger-config") || p.startsWith("/h2-console"))
            return true;

        // Autenticação
        if (p.startsWith("/auth/"))
            return true;

        // ----------- UBS -----------
        // • POST /ubs  – registro de nova UBS
        // • GET  /ubs  e /ubs/{cnes}  – consultas públicas
        if (p.startsWith("/ubs")) {
            return HttpMethod.POST.matches(method) || HttpMethod.GET.matches(method);
        }

        return false;  // demais rotas exigem JWT
    }
}
