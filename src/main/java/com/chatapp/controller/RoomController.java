package com.chatapp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.payload.ApiResponse;
import com.chatapp.payload.CreateRoomRequest;
import com.chatapp.payload.MessageDto;
import com.chatapp.payload.PageResponse;
import com.chatapp.payload.RoomDto;
import com.chatapp.service.RoomService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Create a new chat room. Accepts JSON body e.g. {"roomId": "room-name"} or string value.
     */
    @PostMapping
    public ResponseEntity<RoomDto> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        logger.info("REST request to create room: {}", request.getRoomId());
        RoomDto roomDto = roomService.createRoom(request);
        return new ResponseEntity<>(roomDto, HttpStatus.CREATED);
    }

    /**
     * Get details of a specific room by its roomId.
     */
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoom(@PathVariable String roomId) {
        logger.info("REST request to get room: {}", roomId);
        RoomDto roomDto = roomService.getRoomDtoByRoomId(roomId);
        return ResponseEntity.ok(roomDto);
    }

    /**
     * Get paginated list of all rooms.
     */
    @GetMapping
    public ResponseEntity<PageResponse<RoomDto>> getAllRooms(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "50", required = false) int size) {
        logger.info("REST request to get all rooms [page: {}, size: {}]", page, size);
        PageResponse<RoomDto> response = roomService.getAllRooms(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Get paginated messages for a room.
     */
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<MessageDto>> getMessages(
            @PathVariable String roomId,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size) {
        logger.info("REST request to get messages for room: {} [page: {}, size: {}]", roomId, page, size);
        PageResponse<MessageDto> pageResponse = roomService.getRoomMessages(roomId, page, size);
        return ResponseEntity.ok(pageResponse.getContent());
    }

    /**
     * Delete a chat room.
     */
    @DeleteMapping("/{roomId}")
    public ResponseEntity<ApiResponse<String>> deleteRoom(@PathVariable String roomId) {
        logger.info("REST request to delete room: {}", roomId);
        roomService.deleteRoom(roomId);
        return ResponseEntity.ok(ApiResponse.success("Room deleted successfully", roomId));
    }
}
