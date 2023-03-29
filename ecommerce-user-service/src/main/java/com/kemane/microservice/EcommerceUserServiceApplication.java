package com.kemane.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class EcommerceUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceUserServiceApplication.class, args);
	}

}
