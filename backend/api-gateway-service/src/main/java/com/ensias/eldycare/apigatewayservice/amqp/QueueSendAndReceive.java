package com.ensias.eldycare.apigatewayservice.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueueSendAndReceive {
    @Autowired
    private DirectExchange directExchange;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * true if validation is successful, false otherwise
     * 
     * @param jwt token to validate
     * @return boolean
     */
    public boolean send(String jwt) {
        Boolean response = null;
        int timeout = 100;
        try {
            response = (Boolean) rabbitTemplate.convertSendAndReceive(directExchange.getName(), "rpc", jwt);
            // timeout for response
            while (rabbitTemplate.isRunning() && timeout > 0 && response == null) {
                Thread.sleep(100);
                timeout--;
            }
            if (timeout <= 0 && response == null)
                throw new RuntimeException("Timeout");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return response;
    }
}
