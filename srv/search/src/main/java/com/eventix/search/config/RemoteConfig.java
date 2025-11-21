package com.eventix.search.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Provides a RestTemplate bean with sensible default timeouts.
 */
@Configuration
public class RemoteConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000); // 3s connect
        factory.setReadTimeout(5000);    // 5s read
        return new RestTemplate(factory);
    }
}
