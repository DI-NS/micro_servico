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
    private final long expirationMillis;

    public TokenService(
            @Value("${jwt.secret}") String envKey,
            @Value("${jwt.expiration}") long expirationSeconds
    ) {
        if (envKey == null || envKey.isBlank()) {
            throw new IllegalArgumentException("A variável de ambiente 'JWT_SECRET' deve ser definida.");
        }
        if (expirationSeconds <= 0) {
            throw new IllegalArgumentException("O tempo de expiração deve ser maior que zero.");
        }
        this.secretKey       = Keys.hmacShaKeyFor(envKey.getBytes(StandardCharsets.UTF_8));
        this.expirationMillis = expirationSeconds * 1000;
    }

    /** Gera token para UBS (sub = cnes, claim ubsId = id do usuário) */
    public String generateToken(String cnes, Long ubsId) {
        Date now    = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(cnes)
                .claim("ubsId", ubsId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /** Token “internal” para o serviço UBS‑service */
    public String generateServiceToken() {
        Date now    = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject("ubs-service")
                .claim("service", true)
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
