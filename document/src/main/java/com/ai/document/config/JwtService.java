package com.ai.document.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    // =========================================================
    // SIGNING KEY
    // =========================================================

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    // =========================================================
    // EXTRACT EMAIL
    // =========================================================

    public String extractEmail(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    // =========================================================
    // EXTRACT USER ID
    // =========================================================

    public Long extractUserId(String token) {

        Number userId = extractAllClaims(token)
                .get("userId", Number.class);

        return userId != null
                ? userId.longValue()
                : null;
    }

    // =========================================================
    // EXTRACT ROLE
    // =========================================================

    public String extractRole(String token) {

        return extractAllClaims(token)
                .get("role", String.class);
    }

    // =========================================================
    // EXTRACT ORGANIZATION ID
    // =========================================================

    public Long extractOrganizationId(String token) {

        Number organizationId = extractAllClaims(token)
                .get("organizationId", Number.class);

        return organizationId != null
                ? organizationId.longValue()
                : null;
    }

    // =========================================================
    // VALIDATE TOKEN
    // =========================================================

    public boolean isTokenValid(String token) {

        try {

            Claims claims = extractAllClaims(token);

            Date expiration = claims.getExpiration();

            return expiration != null
                    && expiration.after(new Date());

        } catch (Exception e) {

            return false;
        }
    }

    // =========================================================
    // EXTRACT ALL CLAIMS
    // =========================================================

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}