package com.chatapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.chatapp.entity.MessageType;
import com.chatapp.exception.ResourceAlreadyExistsException;
import com.chatapp.exception.ResourceNotFoundException;
import com.chatapp.payload.CreateRoomRequest;
import com.chatapp.payload.MessageDto;
import com.chatapp.payload.PageResponse;
import com.chatapp.payload.RoomDto;
import com.chatapp.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(RoomController.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomService roomService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testCreateRoom_Success() throws Exception {
        CreateRoomRequest request = new CreateRoomRequest("tech-room");
        RoomDto roomDto = new RoomDto(1L, "tech-room", LocalDateTime.now(), 0L);

        when(roomService.createRoom(any(CreateRoomRequest.class))).thenReturn(roomDto);

        mockMvc.perform(post("/api/v1/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roomId").value("tech-room"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testCreateRoom_Conflict() throws Exception {
        CreateRoomRequest request = new CreateRoomRequest("tech-room");

        when(roomService.createRoom(any(CreateRoomRequest.class)))
                .thenThrow(new ResourceAlreadyExistsException("Room", "roomId", "tech-room"));

        mockMvc.perform(post("/api/v1/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void testGetRoom_Success() throws Exception {
        RoomDto roomDto = new RoomDto(1L, "tech-room", LocalDateTime.now(), 3L);

        when(roomService.getRoomDtoByRoomId("tech-room")).thenReturn(roomDto);

        mockMvc.perform(get("/api/v1/rooms/tech-room"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomId").value("tech-room"))
                .andExpect(jsonPath("$.totalMessages").value(3));
    }

    @Test
    void testGetRoom_NotFound() throws Exception {
        when(roomService.getRoomDtoByRoomId("invalid-room"))
                .thenThrow(new ResourceNotFoundException("Room", "roomId", "invalid-room"));

        mockMvc.perform(get("/api/v1/rooms/invalid-room"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void testGetMessages_Success() throws Exception {
        MessageDto message = new MessageDto(1L, "Alice", "Hello", LocalDateTime.now(), "tech-room", MessageType.CHAT);
        PageResponse<MessageDto> pageResponse = new PageResponse<>(
                Collections.singletonList(message), 0, 20, 1L, 1, true
        );

        when(roomService.getRoomMessages(eq("tech-room"), eq(0), eq(20))).thenReturn(pageResponse);

        mockMvc.perform(get("/api/v1/rooms/tech-room/messages")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sender").value("Alice"))
                .andExpect(jsonPath("$[0].content").value("Hello"));
    }

    @Test
    void testGetAllRooms_Success() throws Exception {
        RoomDto roomDto = new RoomDto(1L, "tech-room", LocalDateTime.now(), 0L);
        PageResponse<RoomDto> pageResponse = new PageResponse<>(
                Collections.singletonList(roomDto), 0, 50, 1L, 1, true
        );

        when(roomService.getAllRooms(eq(0), eq(50))).thenReturn(pageResponse);

        mockMvc.perform(get("/api/v1/rooms")
                .param("page", "0")
                .param("size", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].roomId").value("tech-room"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void testDeleteRoom_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/rooms/tech-room"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
