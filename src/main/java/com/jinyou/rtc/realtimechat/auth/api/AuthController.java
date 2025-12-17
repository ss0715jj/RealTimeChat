package com.jinyou.rtc.realtimechat.auth.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final long accessMinutes;

    public AuthController(
            JwtEncoder jwtEncoder,
            @Value("${app.jwt.issuer}") String issuer,
            @Value("${app.jwt.access-token-minutes}") long accessMinutes
    ) {
        this.jwtEncoder = jwtEncoder;
        this.issuer = issuer;
        this.accessMinutes = accessMinutes;
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid LoginRequest req) {
        log.info("[AuthController] login ------ req : {}", req);

        // TODO : 추후 DB 조회 및 비밀번호 검증
        if (!req.userName().equals(req.password())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid credentials");
        }

        Instant now = Instant.now();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plus(accessMinutes, ChronoUnit.MINUTES))
                .subject(req.userName())
                .claim("role", "USER")
                .build();

        String token = null;
        try {
            JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
            token = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claimsSet)).getTokenValue();
        } catch (JwtEncodingException e) {
            log.error("[AuthController] JWT encoding error", e);
        }

        return new TokenResponse(token);
    }

    public record LoginRequest(@NotBlank String userName, @NotBlank String password) {}

    public record TokenResponse(String accessToken) {}
}
