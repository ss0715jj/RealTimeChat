package com.jinyou.rtc.realtimechat.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "nickname")
    private String nickname;

    // 업데이트, 삽입 시에는 포함되지 않도록
    @Column(name = "create_at", updatable = false, insertable = false)
    private LocalDateTime createAt;
}
