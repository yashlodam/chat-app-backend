package com.chatapp.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chatapp.entity.Message;
import com.chatapp.entity.MessageType;
import com.chatapp.entity.Room;
import com.chatapp.exception.BadRequestException;
import com.chatapp.payload.MessageDto;
import com.chatapp.payload.MessageRequest;
import com.chatapp.repository.MessageRepository;
import com.chatapp.service.ChatService;
import com.chatapp.service.RoomService;

@Service
public class ChatServiceImpl implements ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);

    private final RoomService roomService;
    private final MessageRepository messageRepository;

    public ChatServiceImpl(RoomService roomService, MessageRepository messageRepository) {
        this.roomService = roomService;
        this.messageRepository = messageRepository;
    }

    @Override
    @Transactional
    public MessageDto sendMessage(String roomId, MessageRequest request) {
        if (request == null || request.getSender() == null || request.getSender().trim().isEmpty()) {
            throw new BadRequestException("Sender cannot be empty");
        }
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new BadRequestException("Message content cannot be empty");
        }

        Room room = roomService.getRoomEntityByRoomId(roomId);

        Message message = new Message();
        message.setSender(request.getSender().trim());
        message.setContent(request.getContent().trim());
        message.setTimeStamp(LocalDateTime.now());
        message.setMessageType(request.getMessageType() != null ? request.getMessageType() : MessageType.CHAT);
        message.setRoom(room);

        Message savedMessage = messageRepository.save(message);

        logger.info("New message saved [Room: {}, Sender: {}, Type: {}]", roomId, savedMessage.getSender(), savedMessage.getMessageType());

        return new MessageDto(
                savedMessage.getId(),
                savedMessage.getSender(),
                savedMessage.getContent(),
                savedMessage.getTimeStamp(),
                room.getRoomId(),
                savedMessage.getMessageType()
        );
    }
}
