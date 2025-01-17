package com.ojt.klb.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${spring.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${spring.jwt.expiration}")
    private long expiration;

    public String createToken(String username, String userId, String accountId, String role, String customerId,
            String savingAccountId, String accountNumber) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("userId", userId);
        claims.put("accountId", accountId);
        claims.put("customerId", customerId);
        claims.put("role", "ROLE_" + role);
        claims.put("savingAccountId", savingAccountId);
        claims.put("accountNumber", accountNumber);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Duration getTokenExpiryDuration(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        Date expiration = claims.getExpiration();
        long durationInMillis = expiration.getTime() - System.currentTimeMillis();
        return Duration.ofMillis(durationInMillis);
    }

}