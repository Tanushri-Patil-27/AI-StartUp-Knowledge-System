package com.ai.user.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println();
        System.out.println("========================================");
        System.out.println("JWT FILTER STARTED");
        System.out.println("Request: "
                + request.getMethod()
                + " "
                + request.getRequestURI());

        String authHeader =
                request.getHeader("Authorization");

        System.out.println(
                "Authorization header exists: "
                        + (authHeader != null)
        );

        if (authHeader != null) {
            System.out.println(
                    "Authorization starts with Bearer: "
                            + authHeader.startsWith("Bearer ")
            );

            System.out.println(
                    "Authorization header length: "
                            + authHeader.length()
            );
        }

        String token = null;
        String email = null;

        // =====================================================
        // GET TOKEN
        // =====================================================

        if (authHeader != null &&
                authHeader.startsWith("Bearer ")) {

            token = authHeader.substring(7);

            System.out.println(
                    "JWT token extracted: "
                            + (token != null && !token.isBlank())
            );

            System.out.println(
                    "JWT token length: "
                            + (token != null ? token.length() : 0)
            );

            try {

                email =
                        jwtService.extractEmail(token);

                System.out.println(
                        "Email extracted from JWT: "
                                + email
                );

            } catch (Exception e) {

                System.out.println(
                        "========== JWT EXTRACTION FAILED =========="
                );

                System.out.println(
                        "Error: " + e.getMessage()
                );

                e.printStackTrace();

                System.out.println(
                        "==========================================="
                );

                email = null;
            }

        } else {

            System.out.println(
                    "NO VALID BEARER TOKEN FOUND"
            );
        }

        // =====================================================
        // AUTHENTICATE USER
        // =====================================================

        if (email != null &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

            try {

                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(email);

                System.out.println(
                        "User found in database: "
                                + userDetails.getUsername()
                );

                System.out.println(
                        "User authorities: "
                                + userDetails.getAuthorities()
                );

                boolean valid =
                        jwtService.isTokenValid(
                                token,
                                userDetails.getUsername()
                        );

                System.out.println(
                        "JWT valid: " + valid
                );

                if (valid) {

                    UsernamePasswordAuthenticationToken
                            authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    authentication
                            );

                    System.out.println(
                            "AUTHENTICATION SUCCESS"
                    );

                    System.out.println(
                            "Final authorities: "
                                    + authentication
                                            .getAuthorities()
                    );

                } else {

                    System.out.println(
                            "!!!!!!!! JWT INVALID !!!!!!!!"
                    );
                }

            } catch (Exception e) {

                System.out.println(
                        "========== USER AUTHENTICATION FAILED =========="
                );

                System.out.println(
                        "Error: " + e.getMessage()
                );

                e.printStackTrace();

                System.out.println(
                        "================================================="
                );
            }
        }

        System.out.println(
                "JWT FILTER END"
        );

        System.out.println(
                "========================================"
        );

        filterChain.doFilter(
                request,
                response
        );
    }
}