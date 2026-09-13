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


        /*
         * No Authorization header.
         */
        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }


        String token =
                authorizationHeader.substring(7);


        try {

            /*
             * Validate JWT.
             */
            if (!jwtService.isTokenValid(token)) {

                filterChain.doFilter(request, response);
                return;
            }


            /*
             * Extract information from JWT.
             */
            String email =
                    jwtService.extractEmail(token);

            String role =
                    jwtService.extractRole(token);

            Long userId =
                    jwtService.extractUserId(token);


            /*
             * Prefer userId as authentication name.
             *
             * Organization access checks need user ID.
             */
            String principal;

            if (userId != null) {
                principal = userId.toString();
            } else {
                /*
                 * Temporary fallback until User Service
                 * JWT contains userId.
                 */
                principal = email;
            }


            /*
             * Create authorities.
             */
            List<SimpleGrantedAuthority> authorities =
                    role == null
                            ? List.of()
                            : List.of(
                                new SimpleGrantedAuthority(
                                    "ROLE_" + role
                                )
                            );


            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            authorities
                    );


            /*
             * Store authentication in SecurityContext.
             */
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