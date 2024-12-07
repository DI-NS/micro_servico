package MedMap.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class TokenService {

    private final SecretKey secretKey;
    private final long expiration;

    public TokenService(@Value("${jwt.expiration}") long expiration) {
        // Tenta ler a chave da variável de ambiente
        String envKey = System.getenv("JWT_SECRET");
        if (envKey == null || envKey.isBlank()) {
            // Gera uma nova chave aleatória
            SecretKey generatedKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            System.out.println("ATENÇÃO: Nenhuma JWT_SECRET fornecida. Gerando chave aleatória:");
            System.out.println(new String(generatedKey.getEncoded(), StandardCharsets.UTF_8));
            this.secretKey = generatedKey;
        } else {
            // Usa a chave do ambiente
            this.secretKey = Keys.hmacShaKeyFor(envKey.getBytes(StandardCharsets.UTF_8));
        }

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
        // Este método no Auth-Service é usado internamente se precisar
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
