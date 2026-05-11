package com.eventbooking.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    // Alt+Insert → Constructor → select jwtFilter → OK
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                // WHY: Disable CSRF for REST APIs
                // CSRF protection is for browser sessions
                // We use JWT tokens instead
                .csrf(csrf -> csrf.disable())

                // WHY: Define which endpoints need token
                .authorizeHttpRequests(auth -> auth

                        // WHY: These endpoints are PUBLIC
                        // Anyone can register or login
                        .requestMatchers(
                                "/api/users/register",
                                "/api/users/login"
                        ).permitAll()

                        // WHY: All other endpoints need valid token
                        .anyRequest().authenticated()
                )

                // WHY: Stateless means no server sessions
                // Each request must carry its own token
                // Perfect for microservices
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS)
                )

                // WHY: Add our JWT filter before
                // Spring's default authentication filter
                // So token is validated first
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}