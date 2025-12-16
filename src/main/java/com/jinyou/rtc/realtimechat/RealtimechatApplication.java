package com.jinyou.rtc.realtimechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;

@SpringBootApplication
public class RealtimechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealtimechatApplication.class, args);
    }

}
