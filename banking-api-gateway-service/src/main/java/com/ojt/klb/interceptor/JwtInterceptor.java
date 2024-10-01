package com.ojt.klb.interceptor;

import com.ojt.klb.exception.ErrorResponseHandler;
import com.ojt.klb.response.ApiResponse;
import com.ojt.klb.security.JwtUtil;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Map;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final RateLimiter rateLimiter;
    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;

        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(2)
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .timeoutDuration(Duration.ZERO)
                .build();

        this.rateLimiter = RateLimiter.of("serviceRateLimiter", config);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");

        logger.info("Processing request for URI: {}", request.getRequestURI());

        if (!rateLimiter.acquirePermission()) {
            logger.warn("Too many requests from IP: {}", request.getRemoteAddr());
            ErrorResponseHandler.setErrorResponse(response, HttpStatus.TOO_MANY_REQUESTS.value(), "Too many requests");
            return false;
        }

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if (!jwtUtil.isTokenValid(token)) {
                logger.warn("Invalid token for request URI: {}", request.getRequestURI());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
                return false;
            }

            String username = jwtUtil.extractUserName(token);
            logger.info("Username found: {}", username);
            String userId = jwtUtil.extractUserId(token);
            logger.info("UserId : {}", userId);
            String accountId = jwtUtil.extractAccountId(token);
            logger.info("AccountId : {}", accountId);
            String customerId = jwtUtil.extractCustomerId(token);
            logger.info("CustomerId : {}", customerId);
            String role = jwtUtil.extractRole(token);
            logger.info("Role : {}", role);
            String savingAccountId = jwtUtil.extractSavingAccountId(token);
            logger.info("SavingAccountId : {}", savingAccountId);

            String url = request.getRequestURI();

            Map<String, String> urlMappings = Map.of(
                    "account", "http://localhost:8080",
                    "customer", "http://localhost:8082",
                    "notification", "http://localhost:8083",
                    "reports", "http://localhost:8086"
            );

            for (Map.Entry<String, String> entry : urlMappings.entrySet()) {
                if (url.contains(entry.getKey())) {
                    url = url.replace("userId", userId)
                            .replace("accountId", accountId)
                            .replace("customerId", customerId)
                            .replace("savingAccountId", savingAccountId);
                    url = entry.getValue() + url;
                    if ("customer".equals(entry.getKey())) {
                        logger.info(url);
                    }
                    break;
                }
            }

            url = url.replace("%7B", "").replace("%7D", "");

            logger.info("Processed URL: {}", url);

            if (!request.getRequestURI().equals(url)) {
                logger.info("Redirecting to: {}", url);
                response.sendRedirect(url);
                return false;
            } else {
                logger.error("No replacement was made in the URL. Current URL: {}", request.getRequestURI());
            }
        } else {
            logger.warn("No Authorization header found for request URI: {}", request.getRequestURI());
        }

        return true;
    }
}
