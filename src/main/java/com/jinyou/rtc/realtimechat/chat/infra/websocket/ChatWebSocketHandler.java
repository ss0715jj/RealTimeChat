package com.jinyou.rtc.realtimechat.chat.infra.websocket;

import com.jinyou.rtc.realtimechat.chat.application.ChatCommandService;
import com.jinyou.rtc.realtimechat.chat.infra.support.protocol.IncomingFrame;
import com.jinyou.rtc.realtimechat.chat.infra.support.protocol.OutgoingFrame;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final SessionRegistry sessionRegistry;
    private final MessageSender messageSender;
    private final ChatCommandService chatCommandService;

    public ChatWebSocketHandler(
            ObjectMapper objectMapper,
            SessionRegistry sessionRegistry,
            MessageSender messageSender,
            ChatCommandService chatCommandService
    ) {
        this.objectMapper = objectMapper;
        this.sessionRegistry = sessionRegistry;
        this.messageSender = messageSender;
        this.chatCommandService = chatCommandService;
    }

    // WebSocket이 연결이 되어 사용할 준비가 됨
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        String userId = (String) session.getAttributes().get("userId");
        if (userId == null) {
            closeQuietly(session);
            return;
        }
        sessionRegistry.register(userId, session);
    }

    // 새로운 WebSocket 메세지가 도착했을 때 호출
    @Override
    protected void handleTextMessage(
            @NonNull WebSocketSession session,
            @NonNull TextMessage message
    ) {
        String fromUserId = (String) session.getAttributes().get("userId");
        if (fromUserId == null) {
            closeQuietly(session);
            return;
        }

        // 전달된 메세지를 IncomingFrame 객체에 맞게 변환
        IncomingFrame incomingFrame = objectMapper.readValue(message.getPayload(), IncomingFrame.class);
        OutgoingFrame response = chatCommandService.handle(fromUserId, incomingFrame);

        // 메세지 전달 결과를 발신자에게 응답
        messageSender.send(session, response);
    }

    // WebSocket 연결이 닫혔을 때
    @Override
    public void afterConnectionClosed(
            @NonNull WebSocketSession session,
            @NonNull CloseStatus status
    ) {
        // 끊긴 유저의 id를 세션에서 가져와서 접속된 세션 목록에서 지워준다
        String userId = (String) session.getAttributes().get("userId");
        if (userId != null) {
            sessionRegistry.unregister(userId, session);
        }
    }

    private void closeQuietly(WebSocketSession session) {
        // 세션을 끊어준다
        try {
            session.close();
        } catch (IOException ignored) {}
    }
}
