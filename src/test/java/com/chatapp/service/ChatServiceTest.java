package com.chatapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chatapp.entity.Message;
import com.chatapp.entity.MessageType;
import com.chatapp.entity.Room;
import com.chatapp.exception.BadRequestException;
import com.chatapp.exception.ResourceNotFoundException;
import com.chatapp.payload.MessageDto;
import com.chatapp.payload.MessageRequest;
import com.chatapp.repository.MessageRepository;
import com.chatapp.service.impl.ChatServiceImpl;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private RoomService roomService;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private ChatServiceImpl chatService;

    private Room testRoom;

    @BeforeEach
    void setUp() {
        testRoom = new Room("general");
        testRoom.setId(1L);
    }

    @Test
    void testSendMessage_Success() {
        MessageRequest request = new MessageRequest("Bob", "Hello everyone!", "general", MessageType.CHAT);
        Message savedMessage = new Message(1L, "Bob", "Hello everyone!", LocalDateTime.now(), testRoom, MessageType.CHAT);

        when(roomService.getRoomEntityByRoomId("general")).thenReturn(testRoom);
        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

        MessageDto result = chatService.sendMessage("general", request);

        assertNotNull(result);
        assertEquals("Bob", result.getSender());
        assertEquals("Hello everyone!", result.getContent());
        assertEquals("general", result.getRoomId());
        assertEquals(MessageType.CHAT, result.getMessageType());
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void testSendMessage_RoomNotFound() {
        MessageRequest request = new MessageRequest("Bob", "Hello", "non-existent", MessageType.CHAT);

        when(roomService.getRoomEntityByRoomId("non-existent"))
                .thenThrow(new ResourceNotFoundException("Room", "roomId", "non-existent"));

        assertThrows(ResourceNotFoundException.class, () -> {
            chatService.sendMessage("non-existent", request);
        });
    }

    @Test
    void testSendMessage_EmptySender() {
        MessageRequest request = new MessageRequest("", "Hello", "general", MessageType.CHAT);

        assertThrows(BadRequestException.class, () -> {
            chatService.sendMessage("general", request);
        });
    }

    @Test
    void testSendMessage_EmptyContent() {
        MessageRequest request = new MessageRequest("Bob", "  ", "general", MessageType.CHAT);

        assertThrows(BadRequestException.class, () -> {
            chatService.sendMessage("general", request);
        });
    }
}
