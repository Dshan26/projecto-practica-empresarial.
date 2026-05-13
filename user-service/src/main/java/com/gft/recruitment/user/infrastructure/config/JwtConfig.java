package com.gft.recruitment.user.infrastructure.config;

import com.gft.recruitment.user.domain.model.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtConfig {

    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtConfig(
            @Value("${jwt.secret:defaultSecretKeyThatShouldBeAtLeast256BitsLongForHS256Algorithm}") String secret,
            @Value("${jwt.expiration-ms:86400000}") long expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String generateToken(UUID userId, String email, UserRole rol) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .claim("rol", rol.name())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getEmailFromToken(String token) {
        return validateToken(token).get("email", String.class);
    }

    public String getRolFromToken(String token) {
        return validateToken(token).get("rol", String.class);
    }

    public String getUserIdFromToken(String token) {
        return validateToken(token).getSubject();
    }
}
