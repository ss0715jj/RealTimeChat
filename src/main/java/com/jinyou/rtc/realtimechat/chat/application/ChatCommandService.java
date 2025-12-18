package com.jinyou.rtc.realtimechat.chat.application;

import com.jinyou.rtc.realtimechat.chat.infra.support.protocol.IncomingFrame;
import com.jinyou.rtc.realtimechat.chat.infra.support.protocol.OutgoingFrame;
import com.jinyou.rtc.realtimechat.chat.infra.websocket.MessageSender;
import com.jinyou.rtc.realtimechat.chat.infra.websocket.SessionRegistry;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;

@Slf4j
@Service
public class ChatCommandService {

    private final SessionRegistry sessionRegistry;
    private final MessageSender messageSender;

    public ChatCommandService(SessionRegistry sessionRegistry, MessageSender messageSender) {
        this.sessionRegistry = sessionRegistry;
        this.messageSender = messageSender;
    }

    @NotNull
    public OutgoingFrame handle(String fromUserId, IncomingFrame frame) {
        log.info("[ChatCommandService] handle ------ \nfromUserId : {}\nframe : {}", fromUserId, frame);

        // 보내는 메세지가 없거나 타입이 없을 경우 INVALID_FRAME error 응답
        if (frame == null || frame.type() == null) {
            return OutgoingFrame.error(null, "INVALID_FRAME", "type is required");
        }

        // 보내는 메세지가 있지만 타입이 chat이 아닐경우 UNSUPPORTED_TYPE error 응답
        if (!"CHAT".equals(frame.type())) {
            return OutgoingFrame.error(frame.clientMessageId(), "UNSUPPORTED_TYPE", "unsupported type: " + frame.type());
        }

        // 보내는 메세지에 받는 유저 id가 없거나 공백일 경우 INVALID_TO_USER error 응답
        if (frame.toUserId() == null || frame.toUserId().isBlank()) {
            return OutgoingFrame.error(frame.clientMessageId(), "INVALID_TO_USER", "toUserId is required");
        }

        // 보내는 메세지에 실제 내용이 없거나 공백일 경우 INVALID_CONTENT error 응답
        if (frame.content() == null || frame.content().isBlank()) {
            return OutgoingFrame.error(frame.clientMessageId(), "INVALID_CONTENT", "content is required");
        }

        /*
         * 보내는 메세지에서 메세지를 받는 상대 유저의 id값을 통해 세션을 가져온 후
         * 상대 세션이 존재할 경우 메세지를 전달하고 ack를 응답한다
         */
        Optional<WebSocketSession> targetOpt = sessionRegistry.find(frame.toUserId());
        if (targetOpt.isPresent()) {
            WebSocketSession target = targetOpt.get();
            messageSender.send(target, OutgoingFrame.chat(fromUserId, frame.toUserId(), frame.content(), frame.clientMessageId()));
            return OutgoingFrame.ack(frame.clientMessageId());
        }

        return OutgoingFrame.offline(frame.toUserId());
    }
}
