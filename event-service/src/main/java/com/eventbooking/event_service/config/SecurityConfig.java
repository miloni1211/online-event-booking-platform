package com.eventbooking.event_service.config;

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

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // WHY: Anyone can view active events
                        // No login needed to browse events
                        .requestMatchers(
                                "/api/events/active",
                                "/api/events/search"
                        ).permitAll()

                        // WHY: Only ORGANIZER can create
                        // update or cancel events
                        .requestMatchers(
                                org.springframework.http.HttpMethod.POST,
                                "/api/events/**"
                        ).hasRole("ORGANIZER")

                        .requestMatchers(
                                org.springframework.http.HttpMethod.PUT,
                                "/api/events/**"
                        ).hasRole("ORGANIZER")

                        .requestMatchers(
                                org.springframework.http.HttpMethod.PATCH,
                                "/api/events/**"
                        ).hasRole("ORGANIZER")

                        // WHY: All other requests need auth
                        .anyRequest().authenticated()
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}