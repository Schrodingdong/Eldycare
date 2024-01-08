package com.eldycare.notification.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // internal broker to hold the messages
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/alert");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // STOMP endpoint for websocket client or sockjs if the client does not support websocket
        registry.addEndpoint("/notifications-ws")
//                .setHandshakeHandler() //TODO : https://www.youtube.com/watch?v=LdQY-OUM2mk
                .setAllowedOriginPatterns("*");
        registry.addEndpoint("/notifications-ws")
//                .setHandshakeHandler()
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}