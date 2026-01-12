package com.jinyou.rtc.realtimechat.global.exception;

import lombok.Getter;

/**
 * 비즈니스 애러를 Wrapping
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode code;

    public BusinessException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
