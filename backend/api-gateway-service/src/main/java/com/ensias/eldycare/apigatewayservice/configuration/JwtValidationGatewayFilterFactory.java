package com.ensias.eldycare.apigatewayservice.configuration;

import com.ensias.eldycare.apigatewayservice.amqp.QueueSendAndReceive;
import com.ensias.eldycare.apigatewayservice.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * This class is used to validate JWTs.
 * It is a GatewayFilterFactory, which means it is a filter that can be applied to a route.
 */
@Component
public class JwtValidationGatewayFilterFactory
        extends AbstractGatewayFilterFactory<JwtValidationGatewayFilterFactory.Config> {
    @Autowired
    private QueueSendAndReceive queue;
    private final Logger LOG = LoggerFactory.getLogger(JwtValidationGatewayFilterFactory.class);
    private static Boolean IS_JWT_VALID = null;
    private int count = 0;
    private final int MAX_COUNT = 500;

    public JwtValidationGatewayFilterFactory(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Check if the Auth header exists
            String jwt = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (jwt == null) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            // JWT Extraction
            String extractedJwt = JwtUtils.extractJwt(jwt);
            // JWT Send for validation
            IS_JWT_VALID = null;
            boolean response = queue.send(extractedJwt);
            LOG.info("Response from JWT validation: " + response);
            if(!response){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            // add header with the subejct in it
            String extractedUserEmail =JwtUtils.getSubjectFromToken(extractedJwt);
            LOG.info("The extracted User Email : " + extractedUserEmail);
            ServerHttpRequest req = exchange.getRequest().mutate()
                    .header("User-Email", extractedUserEmail)
                    .build();
            ServerWebExchange exchangeWithNewHeader = exchange.mutate()
                    .request(req)
                    .build();
            return chain.filter(exchangeWithNewHeader);
        };
    }

    /**
     * Store the response of the JWT validation for use in the GatewayFilter
     */
    public static void setIsJwtValid(boolean isJwtValid){
        IS_JWT_VALID = isJwtValid;
    }

    public static class Config{}
}