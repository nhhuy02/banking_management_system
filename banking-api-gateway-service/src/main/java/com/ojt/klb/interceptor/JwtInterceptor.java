package com.ojt.klb.interceptor;

import com.ojt.klb.exception.ErrorResponseHandler;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            String userId = jwtUtil.extractUserId(token);
            String accountId = jwtUtil.extractAccountId(token);
            String customerId = jwtUtil.extractCustomerId(token);
            String savingAccountId = jwtUtil.extractSavingAccountId(token);

            String url = request.getRequestURI();
            url = url.replace("%7B", "").replace("%7D", "");

            String regex = "(?<!v)\\d+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(url);

            Map<String, String> urlMappings = Map.of(
                    "account", "http://localhost:8080",
                    "customer", "http://localhost:8082",
                    "notification", "http://localhost:8083",
                    "reports", "http://localhost:8086"
            );

            String targetUrl = null;

            for (Map.Entry<String, String> entry : urlMappings.entrySet()) {
                logger.info("Checking URL mapping for: {}", entry.getKey());
                if (url.contains(entry.getKey())) {
                    targetUrl = entry.getValue();

                    url = url.replace("userId", userId)
                            .replace("accountId", accountId)
                            .replace("customerId", customerId)
                            .replace("savingAccountId", savingAccountId);


                    while (matcher.find()) {
                        String foundId = matcher.group();
                        logger.info("Found ID in URL: {}", foundId);

                        if (!foundId.equals(userId) && !foundId.equals(accountId)
                                && !foundId.equals(customerId) && !foundId.equals(savingAccountId)) {
                            logger.error("The ID found in the URL does not match any of the IDs from the token.");
                            ErrorResponseHandler.setErrorResponse(response, HttpStatus.FORBIDDEN.value(), "Unauthorized: You not have access to the resource!");
                            return false;
                        }
                    }

                    break;
                }
            }

            if (targetUrl != null) {
                url = targetUrl + url;
                logger.info("Processed URL: {}", url);

                if (!request.getRequestURI().equals(url)) {
                    logger.info("Redirecting to: {}", url);
                    response.sendRedirect(url);
                    return false;
                }
            } else {
                logger.error("No matching URL found in urlMappings.");
            }
        } else {
            logger.warn("No Authorization header found for request URI: {}", request.getRequestURI());
        }

        return true;
    }

}