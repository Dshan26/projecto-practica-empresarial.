package com.gft.recruitment.cv.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com.gft.recruitment.cv")
@EnableDiscoveryClient
public class CvProcessingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CvProcessingServiceApplication.class, args);
    }
}
