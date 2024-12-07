package com.medicamento.medicamento_microservico.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Service
public class TokenService {

    private final SecretKey secretKey;

    public TokenService(@Value("${jwt.secret}") String envKey) {
        if (envKey == null || envKey.isBlank()) {
            throw new RuntimeException("A chave JWT_SECRET não está definida no ambiente. Defina antes de iniciar.");
        }
        this.secretKey = Keys.hmacShaKeyFor(envKey.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();

        return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
    }
}
