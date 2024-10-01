package com.ojt.klb.filter;

import com.ojt.klb.exception.ErrorResponseHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${spring.jwt.secret-key}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtSecret)
                        .parseClaimsJws(jwtToken)
                        .getBody();

                String username = claims.getSubject();
                String role = (String) claims.get("role");

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    List<String> allowedRoles = List.of("ROLE_employee", "ROLE_admin");

                    if (role == null || !allowedRoles.contains(role)) {
                        ErrorResponseHandler.setErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Forbidden: You don't have permission to access this resource");
                        return;
                    }

                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(role));

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            username, null, authorities
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token has expired");
                ErrorResponseHandler.setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT Token has expired");
                return;
            } catch (SignatureException e) {
                logger.error("JWT Token signature validation failed");
                ErrorResponseHandler.setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT Token Signature Validation failed");
                return;
            } catch (Exception e) {
                logger.error("JWT Token validation failed: " + e.getMessage());
                ErrorResponseHandler.setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT Token Validation failed");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}

