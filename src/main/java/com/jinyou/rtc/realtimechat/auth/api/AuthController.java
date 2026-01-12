package com.jinyou.rtc.realtimechat.auth.api;

import com.jinyou.rtc.realtimechat.auth.model.LoginRequest;
import com.jinyou.rtc.realtimechat.auth.model.LoginResponse;
import com.jinyou.rtc.realtimechat.auth.model.SignupRequest;
import com.jinyou.rtc.realtimechat.auth.model.SignupResponse;
import com.jinyou.rtc.realtimechat.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public SignupResponse signup(@Valid @RequestBody SignupRequest req) {
        log.info("[AuthController] signup ----- req : {}", req);

        return authService.signup(req.id(), req.password(), req.nickname());
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        log.info("[AuthController] login ------ req : {}", req);

        return authService.login(req.id(), req.password());
    }
}
