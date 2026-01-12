package com.jinyou.rtc.realtimechat.auth.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotEmpty(message = "아이디는 필수 입력 값 입니다.")
        @Size(max = 20)
        @Pattern(regexp = "^[A-Za-z0-9]+$", message = "아이디는 영문만 가능합니다.")
        String id,

        @NotEmpty(message = "비밀번호는 필수 입력 값 입니다.")
        String password,

        @Size(min = 1, max = 20, message = "닉네임은 1 ~ 20자 까지 입력 가능합니다.")
        String nickname
) {
}
