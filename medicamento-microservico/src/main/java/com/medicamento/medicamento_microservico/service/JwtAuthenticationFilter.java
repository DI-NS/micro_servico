package com.medicamento.medicamento_microservico.service;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public JwtAuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {

        String path = req.getRequestURI();

        // libera GETs públicos e swagger/openapi
        if ((path.startsWith("/medicamento") && req.getMethod().equalsIgnoreCase(HttpMethod.GET.name()))
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")) {
            chain.doFilter(req, res);
            return;
        }

        String header = req.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token ausente ou inválido");
            return;
        }

        String token = header.substring(7);
        if (!tokenService.validateToken(token)) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado");
            return;
        }

        Claims claims = tokenService.getClaims(token);
        String subject = claims.getSubject();
        Long ubsId     = claims.get("ubsId", Long.class);

        // principal = subject (e.g. CNES), credentials = ubsId
        var auth = new UsernamePasswordAuthenticationToken(subject, ubsId, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);

        chain.doFilter(req, res);
    }
}
