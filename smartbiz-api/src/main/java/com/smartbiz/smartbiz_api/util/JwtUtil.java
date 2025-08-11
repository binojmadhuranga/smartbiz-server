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

    public String generateToken(String email, String role) {
        return Jwts.builder().subject(email)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

//    public boolean validateJwtToken(String authToken) {
//        String jwtToken = authToken.substring("Bearer ".length());
//        try {
//            Jwts.parser().setSigningKey(getSigningKey()).build().parse(jwtToken);
//            return true;
//        } catch (Exception e) {
//            System.out.println("Invalid JWT token: {}" + e.getMessage());
//        }
//        return false;
//    }

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
                    .getPayload();
        } catch (JwtException e) {
            System.out.println("Failed to parse JWT claims: " + e.getMessage());
            return null;
        }
    }
}