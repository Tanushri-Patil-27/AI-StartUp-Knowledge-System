package com.ai.user.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    @Value("${jwt.expiration}")
    private long expiration;

    // =========================================================
    // SIGNING KEY
    // =========================================================

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    // =========================================================
    // GENERATE JWT
    // =========================================================

    public String generateToken(
            String email,
            String role,
            Long organizationId) {

        Map<String, Object> claims =
                new HashMap<>();

        claims.put("role", role);
        claims.put("organizationId", organizationId);

        Date now = new Date();

        Date expiryDate =
                new Date(
                        now.getTime() + expiration
                );

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    // =========================================================
    // EXTRACT EMAIL
    // =========================================================

    public String extractEmail(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    // =========================================================
    // EXTRACT ROLE
    // =========================================================

    public String extractRole(String token) {

        return extractAllClaims(token)
                .get("role", String.class);
    }

    // =========================================================
    // EXTRACT ORGANIZATION
    // =========================================================

    public Long extractOrganizationId(
            String token) {

        Number organizationId =
                extractAllClaims(token)
                        .get(
                                "organizationId",
                                Number.class
                        );

        return organizationId != null
                ? organizationId.longValue()
                : null;
    }

    // =========================================================
    // VALIDATE TOKEN
    // =========================================================

    public boolean isTokenValid(
            String token,
            String email) {

        try {

            String tokenEmail =
                    extractEmail(token);

            return tokenEmail.equals(email)
                    && !isTokenExpired(token);

        } catch (Exception e) {

            return false;
        }
    }

    // =========================================================
    // EXPIRATION
    // =========================================================

    private boolean isTokenExpired(
            String token) {

        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // =========================================================
    // CLAIMS
    // =========================================================

    private Claims extractAllClaims(
            String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}