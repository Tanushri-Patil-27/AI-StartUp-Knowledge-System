package com.ai.document.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ai.document.config.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {

        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader =
                request.getHeader("Authorization");

        String token = null;

        String email = null;

        String role = null;

        Long userId = null;

        Long organizationId = null;

        // =====================================================
        // CHECK AUTHORIZATION HEADER
        // =====================================================

        if (authHeader != null &&
                authHeader.startsWith("Bearer ")) {

            token = authHeader.substring(7);

            try {

                email = jwtService.extractEmail(token);

                role = jwtService.extractRole(token);

                userId = jwtService.extractUserId(token);

                organizationId =
                        jwtService.extractOrganizationId(token);

            } catch (Exception e) {

                email = null;

                role = null;

                userId = null;

                organizationId = null;
            }
        }

        // =====================================================
        // CREATE AUTHENTICATION
        // =====================================================

        if (email != null &&
                role != null &&
                userId != null &&
                organizationId != null &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

            if (jwtService.isTokenValid(token)) {

                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority(
                                "ROLE_" + role
                        );

                JwtUserPrincipal principal =
                        new JwtUserPrincipal(
                                userId,
                                email,
                                role,
                                organizationId
                        );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                principal,
                                null,
                                Collections.singletonList(authority)
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}