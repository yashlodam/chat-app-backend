package com.chatapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatAppBackendApplication {

    private static final Logger logger = LoggerFactory.getLogger(ChatAppBackendApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ChatAppBackendApplication.class, args);
        logger.info("ChatApp Backend Application started successfully!");
    }
}
