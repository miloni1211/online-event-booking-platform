package com.eventbooking.user_service.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    // WHY: Reads jwt.secret from application.properties
    @Value("${jwt.secret}")
    private String secret;

    // WHY: Reads jwt.expiration from application.properties
    @Value("${jwt.expiration}")
    private long expiration;

    // WHY: Creates signing key from secret string
    // Key is used to sign and verify tokens
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // GENERATE TOKEN
    // WHY: Creates JWT token when user logs in
    // Token contains email and role
    public String generateToken(String email, String role, String userId) {

        Map<String, Object> claims = new HashMap<>();

        // WHY: Store role in token
        // so we can check permissions without DB call
        claims.put("role", role);
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                // WHY: Subject identifies who the token belongs to
                .setSubject(email)
                // WHY: When token was created
                .setIssuedAt(new Date())
                // WHY: When token expires
                .setExpiration(new Date(
                        System.currentTimeMillis() + expiration))
                // WHY: Sign token with our secret key
                // prevents tampering
                .signWith(getSigningKey(),
                        SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUserId(String token) {
        return (String) extractAllClaims(token).get("userId");
    }

    // EXTRACT EMAIL
    // WHY: Get email from token to identify user
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    // EXTRACT ROLE
    // WHY: Get role from token to check permissions
    public String extractRole(String token) {
        return (String) extractAllClaims(token).get("role");
    }

    // VALIDATE TOKEN
    // WHY: Check if token is genuine and not expired
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            // WHY: Any exception means token is invalid
            return false;
        }
    }

    // CHECK EXPIRY
    // WHY: Expired tokens must be rejected
    private boolean isTokenExpired(String token) {
        Date expiry = extractAllClaims(token).getExpiration();
        return expiry.before(new Date());
    }

    // EXTRACT ALL CLAIMS
    // WHY: Parse token and get all stored data
    // throws exception if token is tampered or invalid
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}