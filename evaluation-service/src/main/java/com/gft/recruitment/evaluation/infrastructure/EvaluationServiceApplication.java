package com.gft.recruitment.evaluation.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com.gft.recruitment.evaluation")
@EnableDiscoveryClient
public class EvaluationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvaluationServiceApplication.class, args);
    }
}
