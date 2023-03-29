package com.kemane.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class EcommerceApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApiGatewayApplication.class, args);
	}

}
