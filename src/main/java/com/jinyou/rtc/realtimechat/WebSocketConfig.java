package com.jinyou.rtc.realtimechat;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;
    private final UserIdHandshakeInterceptor userIdHandshakeInterceptor;

    public WebSocketConfig(
            ChatWebSocketHandler chatWebSocketHandler,
            UserIdHandshakeInterceptor userIdHandshakeInterceptor
    ) {
        this.chatWebSocketHandler = chatWebSocketHandler;
        this.userIdHandshakeInterceptor = userIdHandshakeInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(chatWebSocketHandler, "/ws")
                .addInterceptors(userIdHandshakeInterceptor)
                .setAllowedOriginPatterns("*");
    }
}
