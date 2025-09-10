package com.smartbiz.smartbiz_api.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration; // in milliseconds

    //create a secret key
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(Long userId, String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey()) // Algorithm inferred from key
                .compact();
    }



    public boolean validateJwtToken(String authToken) {
        try {
            String jwtToken = authToken.startsWith("Bearer ") ? authToken.substring(7) : authToken;
            Jwts.parser()
                    .verifyWith((javax.crypto.SecretKey) getSigningKey())
                    .build()
                    .parseSignedClaims(jwtToken);
            return true;
        } catch (JwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }


    public Claims getClaimsFromToken(String authToken) {
        try {
            String jwtToken = authToken.startsWith("Bearer ") ? authToken.substring(7) : authToken;
            return Jwts.parser()
                    .verifyWith((javax.crypto.SecretKey) getSigningKey())
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getBody();
        } catch (JwtException e) {
            System.out.println("Failed to parse JWT claims: " + e.getMessage());
            return null;
        }
    }

    // ===== Extract userId =====
    public Long extractUserId(String authToken) {
        Claims claims = getClaimsFromToken(authToken);
        if (claims != null && claims.get("userId") != null) {
            return Long.valueOf(claims.get("userId").toString());
        }
        return null;
    }

    // ===== Extract email =====
    public String extractEmail(String authToken) {
        Claims claims = getClaimsFromToken(authToken);
        if (claims != null) {
            return claims.getSubject();
        }
        return null;
    }

    // ===== Extract role =====
    public String extractRole(String authToken) {
        Claims claims = getClaimsFromToken(authToken);
        if (claims != null && claims.get("role") != null) {
            return claims.get("role").toString();
        }
        return null;
    }

    // ===== Check if token is expired =====
    public boolean isTokenExpired(String authToken) {
        Claims claims = getClaimsFromToken(authToken);
        if (claims != null) {
            return claims.getExpiration().before(new Date());
        }
        return true; // treat invalid token as expired
    }

}