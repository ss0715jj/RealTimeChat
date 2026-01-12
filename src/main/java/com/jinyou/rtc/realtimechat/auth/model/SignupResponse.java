package com.jinyou.rtc.realtimechat.auth.model;

/**
 * 회원가입 결과 Response 객체
 * @param id 유저 아이디
 * @param nickname 유저 닉네임
 */
public record SignupResponse(
    String id,
    String nickname
) {
}
