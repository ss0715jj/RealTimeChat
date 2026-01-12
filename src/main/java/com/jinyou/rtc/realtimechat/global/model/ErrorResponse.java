package com.jinyou.rtc.realtimechat.global.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jinyou.rtc.realtimechat.global.exception.ErrorCode;
import lombok.Builder;
import org.springframework.http.HttpStatus;

/**
 * 애러 Response
 * @param status 상태코드 (Response 전달에서는 제외)
 * @param code 애러 코드
 * @param message 애러 메세지
 */
@Builder
public record ErrorResponse(
        @JsonIgnore HttpStatus status,
        String code,
        String message
) {

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    public static ErrorResponse of(HttpStatus status, String code, String message) {
        return ErrorResponse.builder()
                .status(status)
                .code(code)
                .message(message)
                .build();
    }
}
