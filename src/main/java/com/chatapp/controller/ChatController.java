package com.chatapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.chatapp.payload.MessageDto;
import com.chatapp.payload.MessageRequest;
import com.chatapp.service.ChatService;

@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Handles real-time messages over STOMP WebSocket.
     * Destination: /app/sendMessage/{roomId}
     * Broadcast to: /topic/room/{roomId}
     */
    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public MessageDto sendMessage(
            @DestinationVariable String roomId,
            @Payload MessageRequest request) {
        logger.info("Received WebSocket message in room: {} from sender: {}", roomId, request.getSender());
        return chatService.sendMessage(roomId, request);
    }
}
