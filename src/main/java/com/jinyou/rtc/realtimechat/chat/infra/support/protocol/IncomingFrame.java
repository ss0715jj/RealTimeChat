package com.jinyou.rtc.realtimechat.chat.infra.support.protocol;


/**
 * WebSocket 메세지의 포멧을 정의
 * @param type 메세지의 타입
 * @param toUserId 메세지를 받을 상대의 Id
 * @param clientMessageId 메세지 구분 Id
 * @param content // 실제 메세지 내용
 */
public record IncomingFrame(
        String type,
        String toUserId,
        String clientMessageId,
        String content
) {  }
