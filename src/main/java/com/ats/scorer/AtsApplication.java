package com.ats.scorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AtsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtsApplication.class, args);
        System.out.println("=================================================");
        System.out.println("  ATS Resume Scorer Backend Engine is Running!  ");
        System.out.println("  Access UI at: http://localhost:8085            ");
        System.out.println("=================================================");
    }
}
