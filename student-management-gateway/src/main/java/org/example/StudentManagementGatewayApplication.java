package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudentManagementGatewayApplication.class, args);
    }
    @Bean
    public RouteLocator routes(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("auth", r -> r.path("/auth/**")
                        .filters(rewrite->rewrite.rewritePath("/auth/(?<extra>.*)", "/${extra}"))
                        .uri("https://www.google.com"))
                .route("student", r -> r.path("/student/**")
                        .uri("http://localhost:8082"))
                .build();

    }

}
