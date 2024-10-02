package com.ojt.klb.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private final JwtUtil jwtUtil;

    @Value("${spring.jwt.secret-key}")
    private String SECRET_KEY ;

    @Value("${spring.jwt.expiration}")
    private long expiration;

    public JwtService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String createToken(String username, String userId, String accountId, String role, String customerId, String savingAccountId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("userId", userId);
        claims.put("accountId", accountId);
        claims.put("customerId", customerId);
        claims.put("role", "ROLE_" + role);
        claims.put("savingAccountId", savingAccountId);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String invalidateToken(String token) {
        Claims claims = jwtUtil.extractAllClaims(token);
        claims.setExpiration(new Date(System.currentTimeMillis() - (expiration * 2)));

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

}