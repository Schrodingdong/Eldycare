package com.ensias.eldycare.apigatewayservice.feign_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "authentication-service")
public interface AuthenticationFeign {
    @PostMapping("/auth/validate-jwt")
    ResponseEntity<?> validateJwt(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt);
}
