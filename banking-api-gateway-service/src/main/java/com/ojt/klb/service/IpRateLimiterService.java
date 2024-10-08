package com.ojt.klb.service;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IpRateLimiterService {

    private final RateLimiterRegistry rateLimiterRegistry;
    private final Map<String, RateLimiter> ipRateLimiters = new ConcurrentHashMap<>();

    public IpRateLimiterService(RateLimiterRegistry rateLimiterRegistry) {
        this.rateLimiterRegistry = rateLimiterRegistry;
    }

    public boolean tryAcquirePermission(String ip) {
        RateLimiter rateLimiter = ipRateLimiters.computeIfAbsent(ip,
                k -> rateLimiterRegistry.rateLimiter(ip));
        return rateLimiter.acquirePermission();
    }
}