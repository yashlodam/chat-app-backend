package com.chatapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.chatapp.entity.Message;
import com.chatapp.entity.MessageType;
import com.chatapp.entity.Room;
import com.chatapp.exception.ResourceAlreadyExistsException;
import com.chatapp.exception.ResourceNotFoundException;
import com.chatapp.payload.CreateRoomRequest;
import com.chatapp.payload.MessageDto;
import com.chatapp.payload.PageResponse;
import com.chatapp.payload.RoomDto;
import com.chatapp.repository.MessageRepository;
import com.chatapp.repository.RoomRepository;
import com.chatapp.service.impl.RoomServiceImpl;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    private Room testRoom;

    @BeforeEach
    void setUp() {
        testRoom = new Room("tech-talk");
        testRoom.setId(1L);
        testRoom.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testCreateRoom_Success() {
        CreateRoomRequest request = new CreateRoomRequest("tech-talk");

        when(roomRepository.existsByRoomId("tech-talk")).thenReturn(false);
        when(roomRepository.save(any(Room.class))).thenReturn(testRoom);

        RoomDto result = roomService.createRoom(request);

        assertNotNull(result);
        assertEquals("tech-talk", result.getRoomId());
        assertEquals(0L, result.getTotalMessages());
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void testCreateRoom_AlreadyExists() {
        CreateRoomRequest request = new CreateRoomRequest("tech-talk");

        when(roomRepository.existsByRoomId("tech-talk")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            roomService.createRoom(request);
        });
    }

    @Test
    void testGetRoomDtoByRoomId_Success() {
        when(roomRepository.findByRoomId("tech-talk")).thenReturn(Optional.of(testRoom));
        when(messageRepository.countByRoom_RoomId("tech-talk")).thenReturn(5L);

        RoomDto result = roomService.getRoomDtoByRoomId("tech-talk");

        assertNotNull(result);
        assertEquals("tech-talk", result.getRoomId());
        assertEquals(5L, result.getTotalMessages());
    }

    @Test
    void testGetRoomDtoByRoomId_NotFound() {
        when(roomRepository.findByRoomId("unknown-room")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            roomService.getRoomDtoByRoomId("unknown-room");
        });
    }

    @Test
    void testGetRoomMessages_Success() {
        Message message = new Message(1L, "Alice", "Hello World!", LocalDateTime.now(), testRoom, MessageType.CHAT);
        Page<Message> messagePage = new PageImpl<>(Collections.singletonList(message));

        when(roomRepository.existsByRoomId("tech-talk")).thenReturn(true);
        when(messageRepository.findByRoom_RoomIdOrderByTimeStampAsc(any(String.class), any(Pageable.class)))
                .thenReturn(messagePage);

        PageResponse<MessageDto> response = roomService.getRoomMessages("tech-talk", 0, 10);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals("Alice", response.getContent().get(0).getSender());
        assertEquals("Hello World!", response.getContent().get(0).getContent());
    }
}
