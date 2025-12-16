package com.jinyou.rtc.realtimechat;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;

@Component
public class UserIdHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            @NonNull  ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @NonNull Map<String, Object> attributes
    ) {
        URI uri = request.getURI();
        String query = uri.getQuery();
        String userId = extractQueryParam(query, "userId");
        if (!StringUtils.hasLength(userId)) return false;

        attributes.put("userId", userId);
        return true;
    }

    @Override
    public void afterHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @Nullable Exception exception
    ) {  }

    @Nullable
    private String extractQueryParam(@Nullable String query, String key) {
        if (query == null) return null;

        for (String part : query.split("&")) {
            String[] kv = part.split("=");
            if (kv.length == 2 && kv[0].equals(key)) return kv[1];
        }

        return null;
    }
}
