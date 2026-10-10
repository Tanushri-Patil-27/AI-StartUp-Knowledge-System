package com.ai.rag.config;

import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {


    private final JwtService jwtService;


    public JwtAuthenticationFilter(
            JwtService jwtService) {

        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {


        String authorizationHeader =
                request.getHeader("Authorization");


        // =====================================================
        // NO JWT
        // =====================================================

        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }


        String token =
                authorizationHeader.substring(7);


        try {

            // =================================================
            // VALIDATE JWT
            // =================================================

            if (!jwtService.isTokenValid(token)) {

                filterChain.doFilter(request, response);
                return;
            }


            // =================================================
            // EXTRACT JWT CLAIMS
            // =================================================

            String email =
                    jwtService.extractEmail(token);

            String role =
                    jwtService.extractRole(token);

            Long userId =
                    jwtService.extractUserId(token);

            Long organizationId =
                    jwtService.extractOrganizationId(token);


            // =================================================
            // REQUIRED CLAIMS
            // =================================================

            if (userId == null) {

                throw new IllegalArgumentException(
                        "JWT does not contain userId"
                );
            }

            if (organizationId == null) {

                throw new IllegalArgumentException(
                        "JWT does not contain organizationId"
                );
            }


            // =================================================
            // CREATE PRINCIPAL
            // =================================================

            JwtUserPrincipal principal =
                    new JwtUserPrincipal(
                            userId,
                            email,
                            role,
                            organizationId
                    );


            // =================================================
            // CREATE AUTHORITIES
            // =================================================

            List<SimpleGrantedAuthority> authorities =
                    role == null
                            ? List.of()
                            : List.of(
                                    new SimpleGrantedAuthority(
                                            "ROLE_" + role
                                    )
                            );


            // =================================================
            // CREATE AUTHENTICATION
            // =================================================

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            authorities
                    );


            // =================================================
            // STORE AUTHENTICATION
            // =================================================

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);


        } catch (JwtException
                | IllegalArgumentException e) {

            SecurityContextHolder
                    .clearContext();
        }


        filterChain.doFilter(
                request,
                response
        );
    }
}