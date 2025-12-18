package com.jinyou.rtc.realtimechat.chat.infra.support.protocol;

public record ErrorPayload(
        String code,
        String message
) { }
