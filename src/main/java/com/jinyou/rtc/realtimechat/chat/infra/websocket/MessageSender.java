package com.jinyou.rtc.realtimechat.chat.infra.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class MessageSender {

    private final ObjectMapper objectMapper;

    public MessageSender(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 세션으로 메세지를 전달한다
     * @param session 메세지를 받을 상대방 세션
     * @param payload 메세지 데이터
     */
    public void send(WebSocketSession session, Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            throw new RuntimeException("failed to send websocket message", e);
        }
    }
}
