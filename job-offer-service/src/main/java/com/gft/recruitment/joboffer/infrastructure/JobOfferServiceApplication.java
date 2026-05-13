package com.gft.recruitment.joboffer.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com.gft.recruitment.joboffer")
@EnableDiscoveryClient
public class JobOfferServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobOfferServiceApplication.class, args);
    }
}
