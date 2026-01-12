package com.jinyou.rtc.realtimechat.auth.service;

import com.jinyou.rtc.realtimechat.auth.entity.UserEntity;
import com.jinyou.rtc.realtimechat.auth.model.LoginResponse;
import com.jinyou.rtc.realtimechat.auth.model.SignupResponse;
import com.jinyou.rtc.realtimechat.auth.repository.UserRepository;
import com.jinyou.rtc.realtimechat.global.config.JwtProvider;
import com.jinyou.rtc.realtimechat.global.exception.BusinessException;
import com.jinyou.rtc.realtimechat.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider
    ) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원가입을 처리한다
     * @param id       유저 아이디
     * @param password 유저 비밀번호
     * @param nickname 유저 닉네임
     */
    @Transactional
    public SignupResponse signup(String id, String password, String nickname) {
        // 이미 등록된 id가 있는지 체크
        // 등록된 id가 있다면 USER_ALREADY_REGISTERED 애러 반환
        if (userRepository.existsByUserId(id)) {
            throw new BusinessException(ErrorCode.USER_ALREADY_REGISTERED);
        }

        // 등록된 닉네임이 있는지 체크
        // 등록된 닉네임이 있다면 DUPLICATE_NICKNAME 애러 반환
        if (userRepository.existsByNickname(nickname)) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(id);
        userEntity.setPasswordHash(passwordEncoder.encode(password));
        userEntity.setNickname(nickname);

        userRepository.save(userEntity);
        return new SignupResponse(id, nickname);
    }

    /**
     * 로그인을 처리한다
     * @param id       유저 아이디
     * @param password 유저 비밀번호
     * @return 로그인 결과 DTO
     */
    public LoginResponse login(String id, String password) {
        UserEntity user = userRepository.findByUserId(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtProvider.createAccessToken(id);
        String refreshToken = jwtProvider.createRefreshToken(id);

        return new LoginResponse(accessToken, refreshToken);
    }
}
