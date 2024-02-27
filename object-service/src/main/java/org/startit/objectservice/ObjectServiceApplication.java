package org.startit.objectservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ObjectServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ObjectServiceApplication.class, args);
    }

}