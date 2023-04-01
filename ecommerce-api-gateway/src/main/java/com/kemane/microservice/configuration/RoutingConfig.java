package com.kemane.microservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutingConfig {

    @Value("hostproduct")
    private String hostproduct;
    @Value("${hostuser}")
    private String hostuser;
    @Value("${hostsale}")
    private String hostsale;
    @Value("${hostorder}")
    private String hostorder;
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                //swagger documentation routes
                .route("ecommerce-product-service", r ->r.path("/api/v1/docs/products/**")
                        .filters(f -> f.rewritePath("^/api/v1/docs/products", ""))
                        .uri(hostproduct))
                .route("ecommerce-user-service", r ->r.path("/api/v1/docs/users/**")
                        .filters(f -> f.rewritePath("^/api/v1/docs/users", ""))
                        .uri(hostuser))
                .route("ecommerce-sale-service", r ->r.path("/api/v1/docs/sales/**")
                        .filters(f -> f.rewritePath("^/api/v1/docs/sales", ""))
                        .uri(hostsale))
                .route("ecommerce-order-service", r ->r.path("/api/v1/docs/orders/**")
                        .filters(f -> f.rewritePath("^/api/v1/docs/orders", ""))
                        .uri(hostorder))

                // routes
                .route("ecommerce-product-service", r ->r.path("/api/v1/products/**")
                        .uri(hostproduct))
                .route("ecommerce-user-service", r ->r.path("/api/v1/users/**")
                        .uri(hostuser))
                .route("ecommerce-sale-service", r ->r.path("/api/v1/sales/**")
                        .uri(hostsale))
                .route("ecommerce-order-service", r ->r.path("/api/v1/orders/**")
                        .uri(hostorder))
                .build();
    }
}
