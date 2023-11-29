package com.example.websocketdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();   //웹소켓의 엔드포인트를 추가합니다. witSockJS()는 웹소켓을 지원하지 않는 브라우저에서도 작동하게 합니다.
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");     // 주제 앞에 /app경로를 고정합니다.
        registry.enableSimpleBroker("/topic");   // topic이라는 주제를 생성
    }
}
