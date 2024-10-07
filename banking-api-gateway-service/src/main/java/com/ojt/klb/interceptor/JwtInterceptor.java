package com.ojt.klb.interceptor;

import com.ojt.klb.exception.ErrorResponseHandler;
import com.ojt.klb.security.JwtUtil;
import com.ojt.klb.service.impl.LoginServiceImpl;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

    private final JwtUtil jwtUtil;
    private final RateLimiter rateLimiter;
    private final LoginServiceImpl loginService;

    public JwtInterceptor(JwtUtil jwtUtil, LoginServiceImpl loginService) {
        this.jwtUtil = jwtUtil;
        this.loginService = loginService;

        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(5)
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .timeoutDuration(Duration.ZERO)
                .build();

        this.rateLimiter = RateLimiter.of("serviceRateLimiter", config);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");
        String referer = request.getHeader("Referer");


        if (!isValidReferer(referer)) {
            logger.warn("Invalid Referer: {}", referer);
            ErrorResponseHandler.setErrorResponse(response, HttpStatus.FORBIDDEN.value(), "Invalid Referer");
            return false;
        }

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
                ErrorResponseHandler.setErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), "Invalid Token");
                return false;
            }

            if (loginService.isTokenBlacklisted(token)) {
                logger.error("JWT Token in blacklisted");
                ErrorResponseHandler.setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Token is blacklisted! Login again get new token");
                return false;
            }

            String userId = jwtUtil.extractUserId(token);
            String accountId = jwtUtil.extractAccountId(token);
            String customerId = jwtUtil.extractCustomerId(token);
            String savingAccountId = jwtUtil.extractSavingAccountId(token);

            String url = request.getRequestURI();

            url = url.replace("%7B", "").replace("%7D", "");

            Map<String, String> urlMappings = Map.of(
                    "/api/v1/account", "http://localhost:8080",
                    "/api/v1/customer", "http://localhost:8082",
                    "/api/v1/notification", "http://localhost:8083",
                    "/api/v1/reports", "http://localhost:8086",
                    "/api/v1/loan-service", "http://localhost:8060",
                    "/api/v1/transactions", "http://localhost:8070",
                    "/api/v1/fund_transfer", "http://localhost:8090"

            );


            String targetUrl = null;

            List<String> urlPatterns = List.of(
                    "/api/v1/account/data/\\d+",
                    "/api/v1/loan-service/loans/\\d+/disburse",
                    "/api/v1/loan-service/loan-applications/\\d+/status",
                    "/api/v1/loan-service/loan-applications/\\d+/loans",
                    "/api/v1/account/users/forgetPassword/code/\\d+",
                    "/api/v1/loan-service/\\d+/repayments/\\d+/pay",
                    "/api/v1/loan-service/\\d+/repayments",
                    "/api/v1/loan-service/collaterals/\\d+/documents",
                    "/api/v1/loan-service/loans/\\d+/disburse",
                    "/api/v1/loan-service/loans/\\d+",
                    "/api/v1/loan-service/loan-applications/\\d+/loans",
                    "/api/v1/loan-service/loan-applications/\\d+/documents",
                    "/api/v1/loan-service/loan-applications/\\d+/documents",
                    "/api/v1/loan-service/loan-applications/\\d+ ",
                    "/api/v1/loan-service/loan-types/\\d+",
                    "/api/v1/notification/getAllNotification?customerId=\\d+&page=\\d+&size=\\d+"
            );

            Map<String, String> urlMappingPrefixes = getUrlMappingPrefixes();

            for (String pattern : urlPatterns) {
                if (url.matches(pattern)) {
                    for (Map.Entry<String, String> entry : urlMappingPrefixes.entrySet()) {
                        String key = entry.getKey();
                        if (pattern.startsWith(key)) {
                            logger.info("Processing URL for {} operations: {}", key.substring(key.lastIndexOf("/") + 1), url);
                            targetUrl = urlMappings.get(key);
                            break;
                        }
                    }

                    if (targetUrl != null) {
                        url = targetUrl + url;
                        logger.info("New URL: {}", url);

                        response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                        response.setHeader("Location", url);

//                        response.sendRedirect(url);
                        return false;
                    }
                }
            }



            for (Map.Entry<String, String> entry : urlMappings.entrySet()) {
                    logger.info("Checking URL mapping for: {}", entry.getKey());
                    if (url.contains(entry.getKey())) {
                        targetUrl = entry.getValue();


                        String regex = "(?<!v)\\d+";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(url);

                        url = url.replace("userId", userId)
                                .replace("accountId", accountId)
                                .replace("customerId", customerId)
                                .replace("savingAccountId", savingAccountId != null ? savingAccountId : "");

                        while (matcher.find()) {
                            String foundId = matcher.group();
                            logger.info("Found ID in URL: {}", foundId);

                            if(foundId.equals("8082")){
                                continue;
                            }

                            if (!foundId.equals(userId) && !foundId.equals(accountId)
                                    && !foundId.equals(customerId) && !foundId.equals(savingAccountId)) {
                                logger.error("The ID found in the URL does not match any of the IDs from the token.");
                                ErrorResponseHandler.setErrorResponse(response, HttpStatus.FORBIDDEN.value(),
                                        "Unauthorized: You not have access to the resource!");
                                return false;
                            }
                        }
                        break;
                    }
                }

//            if (targetUrl != null) {
//                url = targetUrl + url;
//                logger.info("Processed URL: {}", url);
//
//                if (!request.getRequestURI().equals(url)) {
//                    logger.info("Redirecting to: {}", url);
//                    response.sendRedirect(url);
//                    return false;
//                }
//            } else {
//                logger.error("No matching URL found in urlMappings.");
//            }
//        } else {
//            logger.warn("No Authorization header found for request URI: {}", request.getRequestURI());
            if (targetUrl != null) {
                String queryString = request.getQueryString();
                if (queryString != null && !queryString.isEmpty()) {
                    url = targetUrl + url + "?" + queryString;
                } else {
                    url = targetUrl + url;
                }

                logger.info("New URL with query string: {}", url);

                response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                response.setHeader("Location", url);

                return false;
            }
       }

        return true;
    }

    private static Map<String, String> getUrlMappingPrefixes() {
        Map<String, String> urlMappingPrefixes = new HashMap<>();
        urlMappingPrefixes.put("/api/v1/account", "/api/v1/account");
        urlMappingPrefixes.put("/api/v1/loan-service", "/api/v1/loan-service");
        urlMappingPrefixes.put("/api/v1/customer", "/api/v1/customer");
        urlMappingPrefixes.put("/api/v1/reports", "/api/v1/reports");
        urlMappingPrefixes.put("/api/v1/notification", "/api/v1/notification");
        urlMappingPrefixes.put("/api/v1/transactions", "/api/v1/transaction");
        urlMappingPrefixes.put("/api/v1/fund_transfer", "/api/v1/fund_transfer");
        return urlMappingPrefixes;
    }

    private boolean isValidReferer(String referer) {
        return referer != null && referer.startsWith("http://localhost:9999");
    }
}
