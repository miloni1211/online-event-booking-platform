package com.eventbooking.event_service.config;

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
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // Alt+Insert → Constructor → select jwtUtil → OK
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // WHY: Get Authorization header from request
        // Token is sent as: "Bearer eyJhbGc..."
        String authHeader = request
                .getHeader("Authorization");

        String token = null;
        String email = null;

        // WHY: Check if header exists and starts with "Bearer "
        // "Bearer " is standard prefix for JWT tokens
        if (authHeader != null
                && authHeader.startsWith("Bearer ")) {

            // WHY: Extract just the token part
            // Remove "Bearer " prefix (7 characters)
            token = authHeader.substring(7);

            // WHY: Get email from token
            // to identify who is making the request
            email = jwtUtil.extractEmail(token);
        }

        // WHY: If we have valid email and no existing auth
        // Set authentication in Spring Security context
        if (email != null
                && SecurityContextHolder.getContext()
                .getAuthentication() == null) {

            // WHY: Validate token before trusting it
            if (jwtUtil.validateToken(token)) {

                // WHY: Get role from token
                // to set correct permissions
                String role = jwtUtil.extractRole(token);
                String userId = jwtUtil.extractUserId(token);

                // WHY: Create authentication object
                // Spring Security uses this to know
                // who is logged in and what they can do
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                email, userId,
                                // WHY: ROLE_ prefix required by Spring Security
                                List.of(new SimpleGrantedAuthority(
                                        "ROLE_" + role))
                        );

                // WHY: Store authentication in context
                // so Spring Security knows request is authenticated
                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }

        // WHY: Continue to next filter or controller
        // Must always call this or request stops here
        filterChain.doFilter(request, response);
    }
}