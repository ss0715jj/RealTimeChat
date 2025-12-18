package com.jinyou.rtc.realtimechat.chat.infra.support.protocol;

/**
 * Websocket 메세지 전송 결과 포멧을 정의
 * @param type
 * @param clientMessageId 메세지를 구분하기 위한 Id
 * @param toUserId 메세지를 받을 상대 유저 Id
 * @param fromUserId 메세지를 보낸 유저 Id
 * @param content 메세지 내용
 */
public record OutgoingFrame(
        String type,
        String clientMessageId,
        String toUserId,
        String fromUserId,
        String content,
        ErrorPayload errorPayload
) {

    /**
     * 서버가 메세지를 정상적으로 받았음
     * @param clientMessageId 메세지 Id
     * @return 응답 메세지 포멧
     */
    public static OutgoingFrame ack(String clientMessageId) {
        return new OutgoingFrame("ACK", clientMessageId, null, null, null, null);
    }

    /**
     * 연결되어있지 않음
     * @param toUserId 상대방 유저 Id
     * @return 응답 메세지 포멧
     */
    public static OutgoingFrame offline(String toUserId) {
        return new OutgoingFrame("OFFLINE", null, toUserId, null, null, null);
    }

    /**
     * 메세지를 정상적으로 보냄
     * @param fromUserId 메세지를 보내는 유저 Id
     * @param toUserId 메세지를 받는 상대 유저 Id
     * @param content 실제 메세지 내용
     * @param clientMessageId 메세지 Id
     * @return 응답 메세지 포멧
     */
    public static OutgoingFrame chat(String fromUserId, String toUserId, String content, String clientMessageId) {
        return new OutgoingFrame("CHAT", clientMessageId, toUserId, fromUserId, content, null);
    }

    /**
     * 메세지 처리 중 애러발생
     * @param clientMessageId 메세지 Id
     * @param code 애러 코드
     * @param message 애러 메세지
     * @return 응답 메세지 포멧
     */
    public static OutgoingFrame error(String clientMessageId, String code, String message) {
        return new OutgoingFrame("ERROR", clientMessageId, null, null, null, new ErrorPayload(code, message));
    }
}
