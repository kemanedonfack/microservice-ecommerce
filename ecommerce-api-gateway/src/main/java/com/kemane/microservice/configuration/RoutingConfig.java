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

                //swagger documentation routes
                .route("ecommerce-product-service", r ->r.path("/api/v1/docs/products/**")
                        .filters(f -> f.rewritePath("^/api/v1/docs/products", ""))
                        .uri("http://localhost:8083/"))
                .route("ecommerce-user-service", r ->r.path("/api/v1/docs/users/**")
                        .filters(f -> f.rewritePath("^/api/v1/docs/users", ""))
                        .uri("http://localhost:8084/"))
                .route("ecommerce-sale-service", r ->r.path("/api/v1/docs/sales/**")
                        .filters(f -> f.rewritePath("^/api/v1/docs/sales", ""))
                        .uri("http://localhost:8085/"))
                .route("ecommerce-order-service", r ->r.path("/api/v1/docs/orders/**")
                        .filters(f -> f.rewritePath("^/api/v1/docs/orders", ""))
                        .uri("http://localhost:8086/"))

                // routes
                .route("ecommerce-product-service", r ->r.path("/api/v1/products/**")
                        .uri("http://localhost:8083/"))
                .route("ecommerce-user-service", r ->r.path("/api/v1/users/**")
                        .uri("http://localhost:8084/"))
                .route("ecommerce-sale-service", r ->r.path("/api/v1/sales/**")
                        .uri("http://localhost:8085/"))
                .route("ecommerce-order-service", r ->r.path("/api/v1/orders/**")
                        .uri("http://localhost:8086/"))

                .build();
    }
}
