package com.gft.recruitment.ai.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.gft.recruitment.ai")
public class AiAnalysisServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiAnalysisServiceApplication.class, args);
    }
}
