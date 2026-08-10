package com.chatapp.payload;

import java.time.LocalDateTime;

public class RoomDto {

    private Long id;
    private String roomId;
    private LocalDateTime createdAt;
    private long totalMessages;

    public RoomDto() {
    }

    public RoomDto(Long id, String roomId, LocalDateTime createdAt, long totalMessages) {
        this.id = id;
        this.roomId = roomId;
        this.createdAt = createdAt;
        this.totalMessages = totalMessages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getTotalMessages() {
        return totalMessages;
    }

    public void setTotalMessages(long totalMessages) {
        this.totalMessages = totalMessages;
    }
}
