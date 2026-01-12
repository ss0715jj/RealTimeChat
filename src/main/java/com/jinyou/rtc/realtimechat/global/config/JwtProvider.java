package com.jinyou.rtc.realtimechat.global.config;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Slf4j
@Component
public class JwtProvider {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${app.jwt.issuer}")
    String issuer;

    // AccessToken 만료 기간
    @Value("${app.jwt.access-token-expire-time}")
    long accessTokenExpireTime;

    // RefreshToken 만료 기간
    @Value("${app.jwt.refresh-token-expire-time}")
    long refreshTokenExpireTime;

    public JwtProvider(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    @Nullable
    public String createAccessToken(String userId) {
        return createToken("access", userId, accessTokenExpireTime);
    }

    @Nullable
    public String createRefreshToken(String userId) {
        return createToken("refresh", userId, refreshTokenExpireTime);
    }

    @Nullable
    private String createToken(String tokenType, String userId, long expireTime) {
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plus(expireTime, ChronoUnit.MILLIS))
                .claims((claims) -> {
                    claims.put("userId", userId);
                    claims.put("tokenType", tokenType);
                })
                .build();

        return build(claimsSet);
    }

    @Nullable
    private String build(JwtClaimsSet claimsSet) {
        try {
            JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
            return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claimsSet)).getTokenValue();
        } catch (JwtEncodingException e) {
            log.error("[JwtProvider] Encoding Error", e);
            return null;
        }
    }

    @Nullable
    public String getUserId(String token) {
        return (String) parseClaims(token).getOrDefault("userId", null);
    }

    @Nullable
    public String getTokenType(String token) {
        return (String) parseClaims(token).get("tokenType");
    }

    private Map<String, Object> parseClaims(String token) {
        try {
            return jwtDecoder.decode(token).getClaims();
        } catch (JwtException ignored) {
            return Map.of();
        }
    }

    /**
     * 토큰을 검증한다
     * @param token 검증할 토큰
     * @return 검증된 토큰인지 여부
     */
    public boolean isValidToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
