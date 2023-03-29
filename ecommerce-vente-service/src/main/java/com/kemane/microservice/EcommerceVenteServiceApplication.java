package com.kemane.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class EcommerceVenteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceVenteServiceApplication.class, args);
	}

}
