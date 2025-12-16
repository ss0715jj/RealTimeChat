package com.jinyou.rtc.realtimechat;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    // socket 에서 넘어온 데이터를 자바 객체로 변환하기 위해 사용
    private final ObjectMapper objectMapper = new ObjectMapper();
    // 연결된 세션 목록
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();


    // WebSocket이 연결이 되어 사용할 준비가 됨
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        String userId = (String) session.getAttributes().get("userId");
        if (userId == null) {
            closeQuietly(session);
            return;
        }
        sessions.put(userId, session);
    }

    // 새로운 WebSocket 메세지가 도착했을 때 호출
    @Override
    protected void handleTextMessage(
            @NonNull WebSocketSession session,
            @NonNull TextMessage message
    ) throws Exception {
        // 전달된 메세지를 IncomingFrame 객체에 맞게 변환
        IncomingFrame frame = objectMapper.readValue(message.getPayload(), IncomingFrame.class);

        // type이 채팅인 아닌 경우 처리하지 않음
        if (!"CHAT".equals(frame.type)) return;

        // 상대의 id를 통해 세션을 가져온다
        // 상대의 세션을 찾았고 열려있는 경우만 메세지를 전달한다
        WebSocketSession target = sessions.get(frame.toUserId);
        if (target != null && target.isOpen()) {
            // 상대에게
            target.sendMessage(new TextMessage(objectMapper.writeValueAsString(frame)));

            // 나에게
            session.sendMessage(new TextMessage("{\"type\":\"ACK\",\"clientMessageId\":\"" + frame.clientMessageId() + "\"}"));
        }
    }

    // WebSocket 연결이 닫혔을 때
    @Override
    public void afterConnectionClosed(
            @NonNull WebSocketSession session,
            @NonNull CloseStatus status
    ) {
        // 끊긴 유저의 id를 세션에서 가져와서 접속된 세션 목록에서 지워준다
        String userId = (String) session.getAttributes().get("userId");
        if (userId != null) sessions.remove(userId, session);
    }

    private void closeQuietly(WebSocketSession session) {
        // 세션을 끊어준다
        try {
            session.close();
        } catch (IOException ignored) {}
    }

    private record IncomingFrame(
            String type,
            String toUserId,
            String clientMessageId,
            String content
    ) {  }
}
