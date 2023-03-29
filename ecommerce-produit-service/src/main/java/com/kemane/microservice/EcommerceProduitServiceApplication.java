package com.kemane.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class EcommerceProduitServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceProduitServiceApplication.class, args);
	}

}
