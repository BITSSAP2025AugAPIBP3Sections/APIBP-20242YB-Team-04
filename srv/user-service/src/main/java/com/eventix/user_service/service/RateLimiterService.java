package com.eventix.user_service.service;

import com.eventix.user_service.exception.RateLimitExceededException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    /**
     * Rate limit for login attempts: 5 requests per 15 minutes
     */
    public Bucket resolveLoginBucket(String key) {
        return cache.computeIfAbsent(key, k -> createLoginBucket());
    }

    private Bucket createLoginBucket() {
        Bandwidth limit = Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(15)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    /**
     * Rate limit for user registration: 10 requests per hour
     */
    public Bucket resolveRegistrationBucket(String key) {
        return cache.computeIfAbsent(key, k -> createRegistrationBucket());
    }

    private Bucket createRegistrationBucket() {
        Bandwidth limit = Bandwidth.classic(10, Refill.intervally(10, Duration.ofHours(1)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    /**
     * Rate limit for forgot password: 3 requests per 10 minutes
     */
    public Bucket resolveForgotPasswordBucket(String key) {
        return cache.computeIfAbsent(key, k -> createForgotPasswordBucket());
    }

    private Bucket createForgotPasswordBucket() {
        Bandwidth limit = Bandwidth.classic(3, Refill.intervally(3, Duration.ofMinutes(10)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    /**
     * Check if request is allowed, throw exception if rate limit exceeded
     */
    public void checkRateLimit(Bucket bucket, String action) {
        if (!bucket.tryConsume(1)) {
            throw new RateLimitExceededException(
                "Rate limit exceeded for " + action + ". Please try again later."
            );
        }
    }
}
