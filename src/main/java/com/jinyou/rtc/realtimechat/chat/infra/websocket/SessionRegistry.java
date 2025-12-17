package com.jinyou.rtc.realtimechat.chat.infra.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션을 관리하기 위한 클래스
 * 사용하는 곳에서 주입받아 사용한다.
 */
@Component
public class SessionRegistry {

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    /**
     * 세션을 연결한다
     * @param userId 연결할 유저 id
     * @param session 연결할 유저의 session
     */
    public void register(String userId, WebSocketSession session) {
        sessions.put(userId, session);
    }

    /**
     * 세션 연결을 해제한다
     * @param userId 해제할 유저의 id
     * @param session 해제할 유저의 session
     */
    public void unregister(String userId, WebSocketSession session) {
        sessions.remove(userId, session);
    }

    /**
     * 유저 id를 통해 세션을 찾아서 반환한다
     * @param userId 세션을 찾을 유저 id
     * @return 검색된 유저 세션
     */
    public Optional<WebSocketSession> find(String userId) {
        WebSocketSession session = sessions.get(userId);
        if (session == null || !session.isOpen()) return Optional.empty();
        return Optional.of(session);
    }
}
