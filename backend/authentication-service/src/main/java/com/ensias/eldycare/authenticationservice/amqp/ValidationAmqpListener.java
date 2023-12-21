package com.ensias.eldycare.authenticationservice.amqp;

import com.ensias.eldycare.authenticationservice.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ValidationAmqpListener {
    @Autowired
    private AuthService authService;
    private final Logger LOG = LoggerFactory.getLogger(ValidationAmqpListener.class);

    @RabbitListener(queues = "${amqp.auth.queue}")
    public boolean validateJwt(@Payload String jwt) {
        LOG.info("Received JWT: " + jwt);
        boolean validation = authService.validateJWT(jwt);
        LOG.info("JWT validation result: " + validation);
        return validation;
    }
}
