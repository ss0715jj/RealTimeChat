package com.jinyou.rtc.realtimechat.auth.model;

/**
 * 로그인 결과를 나타내는 DTO
 * @param accessToken 액세스 토큰
 * @param refreshToken 리프레시 토큰
 */
public record LoginResponse(String accessToken, String refreshToken) { }
