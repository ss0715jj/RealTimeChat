package com.jinyou.rtc.realtimechat.auth.repository;

import com.jinyou.rtc.realtimechat.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // 유저 아이디를 통해 UserEntity 검색
    Optional<UserEntity> findByUserId(String userId);

    // 이미 등록된 유저 아이디가 있는지 확인
    boolean existsByUserId(String userId);

    // 등록된 유저 닉네임이 있는지 확인
    boolean existsByNickname(String userNickname);
}
