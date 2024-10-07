package com.ojt.klb.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${spring.jwt.secret-key}")
    private String SECRET_KEY;

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUserId(String token) {
        return extractAllClaims(token).get("userId", String.class);
    }

    public String extractAccountId(String token) {
        return extractAllClaims(token).get("accountId", String.class);
    }

    public String extractCustomerId(String token) {
        return extractAllClaims(token).get("customerId", String.class);
    }


    public String extractSavingAccountId(String token) {
        return extractAllClaims(token).get("savingAccountId", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            logger.error("JwtUtil isTokenValid: {}", e.getMessage());
            return false;
        }
    }
}
