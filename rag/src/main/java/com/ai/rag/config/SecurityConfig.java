package com.ai.rag.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {


        http
                /*
                 * Disable CSRF because this is a
                 * stateless REST API.
                 */
                .csrf(csrf -> csrf.disable())


                /*
                 * No HTTP session.
                 */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )


                /*
                 * Endpoint authorization.
                 */
                .authorizeHttpRequests(auth -> auth

                        /*
                         * Health endpoints are public.
                         */
                        .requestMatchers(
                                "/actuator/health",
                                "/actuator/info"
                        ).permitAll()


                        /*
                         * Everything else requires JWT.
                         */
                        .anyRequest().authenticated()
                )


                /*
                 * Our JWT filter runs before
                 * Spring's username/password filter.
                 */
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();
    }
}