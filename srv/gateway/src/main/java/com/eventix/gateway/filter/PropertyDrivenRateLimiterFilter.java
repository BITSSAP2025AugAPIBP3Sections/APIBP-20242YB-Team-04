// package com.eventix.gateway.filter;

// import io.github.bucket4j.Bandwidth;
// import io.github.bucket4j.Bucket;
// import io.github.bucket4j.Bucket4j;
// import io.github.bucket4j.Refill;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.cloud.gateway.filter.GatewayFilterChain;
// import org.springframework.cloud.gateway.filter.GlobalFilter;
// import org.springframework.core.Ordered;
// import org.springframework.cloud.gateway.route.Route;
// import org.springframework.http.MediaType;
// import org.springframework.stereotype.Component;
// import org.springframework.web.server.ServerWebExchange;
// import reactor.core.publisher.Mono;

// import java.nio.charset.StandardCharsets;
// import java.time.Duration;
// import java.util.Map;
// import java.util.concurrent.ConcurrentHashMap;

// /**
//  * Defensive, lazy-initializing rate limiter global filter.
//  * - Does not perform heavy work in constructor
//  * - Catches and logs all exceptions and returns proper HTTP responses instead of closing connection
//  */
// @Component
// public class PropertyDrivenRateLimiterFilter implements GlobalFilter, Ordered {

//     private static final Logger log = LoggerFactory.getLogger(PropertyDrivenRateLimiterFilter.class);

//     private final RateLimitsProperties props;
//     // routeId -> Bucket
//     private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

//     public PropertyDrivenRateLimiterFilter(RateLimitsProperties props) {
//         this.props = props;
//         // don't pre-initialize everything here (avoids NPEs during startup)
//         log.info("PropertyDrivenRateLimiterFilter initialized");
//     }

//     private Bucket createBucketIfNeeded(String routeId) {
//         // double-check id and property existence
//         RateLimitsProperties.Rule rule = props == null ? null : props.getRules().get(routeId);
//         if (rule == null || !rule.isEnabled()) return null;

//         return buckets.computeIfAbsent(routeId, rid -> {
//             try {
//                 Refill refill = Refill.intervally(rule.getRate(), Duration.ofSeconds(1));
//                 Bandwidth limit = Bandwidth.classic(rule.getCapacity(), refill);
//                 Bucket bucket = Bucket4j.builder().addLimit(limit).build();
//                 log.info("Created bucket for routeId={} rate={} capacity={}", rid, rule.getRate(), rule.getCapacity());
//                 return bucket;
//             } catch (Throwable t) {
//                 log.error("Failed to create bucket for routeId={}: {}", routeId, t.toString(), t);
//                 return null;
//             }
//         });
//     }

//     @Override
//     public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//         try {
//             Route route = exchange.getAttribute(org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
//             if (route == null) {
//                 // no route matched yet — let it pass through
//                 return chain.filter(exchange);
//             }

//             String routeId = route.getId();
//             RateLimitsProperties.Rule rule = props == null ? null : props.getRules().get(routeId);
//             if (rule == null || !rule.isEnabled()) {
//                 // not rate limited
//                 return chain.filter(exchange);
//             }

//             Bucket bucket = createBucketIfNeeded(routeId);
//             if (bucket == null) {
//                 // couldn't create bucket -> permit by default (or fail safe)
//                 log.warn("Bucket is null for routeId={}, permitting request", routeId);
//                 return chain.filter(exchange);
//             }

//             if (bucket.tryConsume(1)) {
//                 // allowed
//                 return chain.filter(exchange);
//             } else {
//                 // rate limited -> return 429 with Retry-After header & JSON body
//                 exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.TOO_MANY_REQUESTS);
//                 exchange.getResponse().getHeaders().add("Retry-After", "1");
//                 byte[] body = ("{\"error\":\"Too Many Requests\",\"route\":\"" + routeId + "\"}")
//                         .getBytes(StandardCharsets.UTF_8);
//                 exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
//                 exchange.getResponse().getHeaders().setContentLength(body.length);
//                 return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
//                         .bufferFactory().wrap(body)));
//             }
//         } catch (Throwable ex) {
//             // critical: catch all exceptions and return 500 instead of closing the connection
//             log.error("RateLimiter filter threw exception: {}", ex.toString(), ex);
//             exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
//             exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
//             byte[] body = ("{\"error\":\"gateway_error\",\"message\":\"RateLimiter failure\"}")
//                     .getBytes(StandardCharsets.UTF_8);
//             exchange.getResponse().getHeaders().setContentLength(body.length);
//             try {
//                 return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
//                         .bufferFactory().wrap(body)));
//             } catch (Throwable writeEx) {
//                 log.error("Failed to write error response: {}", writeEx.toString(), writeEx);
//                 // if writing fails, complete without response (last resort)
//                 return exchange.getResponse().setComplete();
//             }
//         }
//     }

//     @Override
//     public int getOrder() {
//         // run early
//         return -100;
//     }
// }
