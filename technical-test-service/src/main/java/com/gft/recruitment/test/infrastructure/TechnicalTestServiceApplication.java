package com.gft.recruitment.test.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com.gft.recruitment.test")
@EnableDiscoveryClient
public class TechnicalTestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechnicalTestServiceApplication.class, args);
    }
}
