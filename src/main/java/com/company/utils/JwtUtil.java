package com.company.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationTime;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // returns SecretKey
    }


    // Generate JWT Token
    public String generateToken(String username, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email",email);
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .claims(claims)
                .signWith(key)
                .compact();
    }

    // Verify and extract claims
    public Claims verifyToken(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(key)
                .build();

        return parser.parseSignedClaims(token).getPayload();
    }

    public int extractUserId(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(key)
                .build();

        Claims claims = parser.parseSignedClaims(token).getPayload();
        String userIdStr = claims.getSubject(); // subject = userId as String
        return Integer.parseInt(userIdStr);     // convert to int
    }

    public String extractEmail(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(key)
                .build();

        Claims claims = parser.parseSignedClaims(token).getPayload();
        return claims.get("email", String.class);
    }

}
