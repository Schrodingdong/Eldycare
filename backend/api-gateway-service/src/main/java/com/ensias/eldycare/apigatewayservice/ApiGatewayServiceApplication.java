package com.ensias.eldycare.apigatewayservice;

import com.ensias.eldycare.apigatewayservice.configuration.JwtValidationGatewayFilterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ApiGatewayServiceApplication {

        public static void main(String[] args) {
                SpringApplication.run(ApiGatewayServiceApplication.class, args);
        }

        @Autowired
        private JwtValidationGatewayFilterFactory jwtValidationFilter;

        @Bean
        public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
                GatewayFilter filter = jwtValidationFilter.apply(new JwtValidationGatewayFilterFactory.Config());
                return builder.routes()
                                .route(r -> r.path("/auth/**")
                                                .uri("lb://AUTHENTICATION-SERVICE"))
//                                .route(r -> r.path("/filter-test/**")
//                                                .filters(f -> f.filter(filter))
//                                                .uri("lb://AUTHENTICATION-SERVICE"))
                                .route(r -> r.path("/users/**")
                                                .filters(f -> f.filter(filter))
                                                .uri("lb://USER-SERVICE"))
                                .route(r -> r.path("/notification/**")
                                                .filters(f -> f.filter(filter))
                                                .uri("lb://NOTIFICATION-SERVICE"))
                                .route(r -> r.path("/reminder/**")
                                        .filters(f -> f.filter(filter))
                                        .uri("lb://REMINDER-SERVICE"))
                                .build();
        }

}
