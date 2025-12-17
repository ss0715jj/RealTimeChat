package com.jinyou.rtc.realtimechat.global.util;

import org.jspecify.annotations.Nullable;

public class Util {

    /**
     * URI 내 query 에서 전달된 key 에 해당하는 값을 추출하여 반환한다
     * @param query URI query 문자열
     * @param key query에서 추출할 key 문자열
     * @return key에 해당하는 값
     */
    @Nullable
    public static String extractQueryParam(@Nullable String query, String key) {
        if (query == null) return null;

        for (String part : query.split("&")) {
            String[] kv = part.split("=");
            if (kv.length == 2 && kv[0].equals(key)) return kv[1];
        }

        return null;
    }
}
