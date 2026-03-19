package com.posstudio.papel.security.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;
    private SecretKey key;
    private final long jwtExpirationMs = 86400000;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(
                java.util.Base64.getDecoder().decode(jwtSecret));
    }

    public String generateToken(UserDetails usuarioAuth) {
        return Jwts.builder()
                .subject(usuarioAuth.getUsername())
                .claim("roles", usuarioAuth.getAuthorities().stream()
                        .map(auth -> auth.getAuthority())
                        .toList())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            throw new RuntimeException(e.toString());
        }
    }
}
