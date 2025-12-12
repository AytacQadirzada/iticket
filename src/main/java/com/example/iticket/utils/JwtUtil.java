package com.example.iticket.utils;

import com.example.iticket.dao.entity.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String ACCESS_TOKEN_SECRET = "f93a7cd8d1c2b6e43fa1a2b1de77c49e2b0671a55f8c2964c633f03b";
    private static final String REFRESH_TOKEN_SECRET = "1e84a97df2c657344da983657e0a4fe5d346b0b981bb8c742d6ab0b3";
    private static final long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000;
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000;

    public String generateAccessToken(UserEntity user, String email) {
        Key key = Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes());
        return Jwts.builder()
                .subject(email)
                .claim("role", user.getRoles())
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("surname", user.getSurname())
                .claim("phone", user.getPhone())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(UserEntity user, String email) {
        Key key = Keys.hmacShaKeyFor(REFRESH_TOKEN_SECRET.getBytes());
        return Jwts.builder()
                .subject(email)
                .claim("role", user.getRoles())
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("surname", user.getSurname())
                .claim("phone", user.getPhone())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(key)
                .compact();
    }

    public String extractUsernameFromAccessToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String extractUsernameFromRefreshToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(REFRESH_TOKEN_SECRET.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

}
