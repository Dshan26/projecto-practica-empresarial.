package com.gft.recruitment.gateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(
            @Value("${jwt.secret:defaultSecretKeyThatShouldBeAtLeast256BitsLongForHS256Algorithm}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUserIdFromToken(String token) {
        return validateToken(token).getSubject();
    }

    public String getEmailFromToken(String token) {
        return validateToken(token).get("email", String.class);
    }

    public String getRoleFromToken(String token) {
        return validateToken(token).get("rol", String.class);
    }
}
