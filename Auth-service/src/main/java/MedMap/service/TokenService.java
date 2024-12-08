package MedMap.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class TokenService {

    private final SecretKey secretKey;
    private final long expiration;

    public TokenService(@Value("${jwt.secret}") String envKey, @Value("${jwt.expiration}") long expiration) {
        if (envKey == null || envKey.isBlank()) {
            throw new IllegalArgumentException("A variável de ambiente 'JWT_SECRET' deve ser definida.");
        }
        if (expiration <= 0) {
            throw new IllegalArgumentException("O tempo de expiração deve ser maior que zero.");
        }
        this.secretKey = Keys.hmacShaKeyFor(envKey.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration * 1000; // converte segundos para milissegundos
    }

    public String generateToken(String cnes) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setSubject(cnes)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
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

    public String getSubjectFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
