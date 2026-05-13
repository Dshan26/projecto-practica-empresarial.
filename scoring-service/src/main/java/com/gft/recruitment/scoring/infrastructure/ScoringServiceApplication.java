package com.gft.recruitment.scoring.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com.gft.recruitment.scoring")
@EnableDiscoveryClient
public class ScoringServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScoringServiceApplication.class, args);
    }
}
