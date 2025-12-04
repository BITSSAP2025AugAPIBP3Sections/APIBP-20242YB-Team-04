package com.eventix.gateway.filter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "gateway.ratelimits")
public class RateLimitsProperties {
    private Map<String, Rule> rules = new HashMap<>();

    public Map<String, Rule> getRules() {
        return rules;
    }

    public void setRules(Map<String, Rule> rules) {
        this.rules = rules;
    }

    public static class Rule {
        private boolean enabled;
        private int rate;
        private int capacity;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public int getRate() { return rate; }
        public void setRate(int rate) { this.rate = rate; }

        public int getCapacity() { return capacity; }
        public void setCapacity(int capacity) { this.capacity = capacity; }
    }
}
