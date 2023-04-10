package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * main application class
 *
 * @author bakhridinova
 */

@SpringBootApplication(scanBasePackages = "com.epam.esm")
public class GiftCertificatesAdvancedApplication {

    public static void main(String[] args) {
        SpringApplication.run(GiftCertificatesAdvancedApplication.class, args);
    }
}