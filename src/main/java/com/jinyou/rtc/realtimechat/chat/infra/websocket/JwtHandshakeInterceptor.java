package com.jinyou.rtc.realtimechat.chat.infra.websocket;

import com.jinyou.rtc.realtimechat.global.util.Util;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;

/**
 * WebSocket 연결 전 인증 여부를 확인하기 위한 Interceptor
 * Http -> WebSocket 승격 전 유저가 로그인 되어있는지 확인하여 로그인 되어있다면
 * WebSocket으로의 승격의 허가하고 로그인이 되어있지 않다면 연결을 거부한다
 */
@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtDecoder jwtDecoder;

    public JwtHandshakeInterceptor(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public boolean beforeHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @NonNull Map<String, Object> attributes
    ) {
        // 요청 uri에서 token을 추출한다
        URI uri = request.getURI();
        String token = Util.extractQueryParam(uri.getQuery(), "token");
        // 토큰 길이가 0이라면 연결을 거부한다
        if (!StringUtils.hasLength(token)) return false;

        try {
            // 토큰을 디코딩하여 유저 id를 가져온다
            Jwt jwt = jwtDecoder.decode(token);
            String userId = jwt.getSubject();
            attributes.put("userId", userId);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    @Override
    public void afterHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @Nullable Exception exception
    ) {}
}
