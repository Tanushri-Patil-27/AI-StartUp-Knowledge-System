package com.ai.rag.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;


    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }


    public String extractEmail(String token) {

        return extractAllClaims(token)
                .getSubject();
    }


    public String extractRole(String token) {

        return extractAllClaims(token)
                .get("role", String.class);
    }


    /**
     * User ID is expected to be stored inside
     * the JWT as the "userId" claim.
     */
    public Long extractUserId(String token) {

        Object userId =
                extractAllClaims(token)
                        .get("userId");

        if (userId == null) {
            return null;
        }

        if (userId instanceof Number number) {
            return number.longValue();
        }

        return Long.parseLong(
                userId.toString()
        );
    }


    public boolean isTokenValid(String token) {

        try {

            Claims claims =
                    extractAllClaims(token);

            return claims.getExpiration() != null
                    && claims.getExpiration()
                    .after(new java.util.Date());

        } catch (Exception e) {

            return false;
        }
    }


    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}