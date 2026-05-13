package com.gft.recruitment.ai.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${openai.api.key:sk-placeholder}")
    private String openAiApiKey;

    @Bean("openAiWebClient")
    public WebClient openAiWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.openai.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openAiApiKey)
                .build();
    }

    @LoadBalanced
    @Bean("serviceWebClientBuilder")
    public WebClient.Builder serviceWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean("serviceWebClient")
    public WebClient serviceWebClient(@org.springframework.beans.factory.annotation.Qualifier("serviceWebClientBuilder") WebClient.Builder builder) {
        return builder.build();
    }
}
