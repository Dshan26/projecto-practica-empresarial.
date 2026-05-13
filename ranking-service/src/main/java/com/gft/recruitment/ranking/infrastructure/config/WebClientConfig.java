package com.gft.recruitment.ranking.infrastructure.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    /**
     * Load-balanced WebClient builder for inter-service calls via Eureka.
     */
    @LoadBalanced
    @Bean("serviceWebClientBuilder")
    public WebClient.Builder serviceWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean("serviceWebClient")
    public WebClient serviceWebClient(
            @org.springframework.beans.factory.annotation.Qualifier("serviceWebClientBuilder")
            WebClient.Builder builder) {
        return builder.build();
    }
}
