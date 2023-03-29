package com.kemane.microservice.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutingConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                //swagger documentation
                .route("ecommerce-produit-service", r ->r.path("/produit/**")
                        .filters(f -> f.rewritePath("^/product", ""))
                        .uri("http://localhost:8083/"))
                .route("ecommerce-user-service", r ->r.path("/user/**")
                        .filters(f -> f.rewritePath("^/user", ""))
                        .uri("http://localhost:8084/"))
                .route("ecommerce-vente-service", r ->r.path("/vente/**")
                        .filters(f -> f.rewritePath("^/vente", ""))
                        .uri("http://localhost:8085/"))
                .build();
    }
}
