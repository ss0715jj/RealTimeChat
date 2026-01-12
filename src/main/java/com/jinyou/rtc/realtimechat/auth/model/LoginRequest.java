package com.jinyou.rtc.realtimechat.auth.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 로그인 요청 객체를 나타내는 DTO
 * @param id 유저 아이디
 * @param password 유저 비밀번호
 */
public record LoginRequest(
        @NotEmpty(message = "아이디는 필수 입력 값 입니다.")
        @Size(max = 20, message = "최대 20자 까지 입력 가능합니다.")
        @Pattern(regexp = "^[A-Za-z0-9]+$", message = "아이디는 영문만 입력 가능합니다.")
        String id,

        @NotEmpty(message = "비밀번호는 필수 입력 값 입니다.")
        String password
) { }
