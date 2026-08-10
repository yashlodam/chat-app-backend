package com.chatapp.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateRoomRequest {

    @NotBlank(message = "Room ID cannot be empty")
    @Size(min = 2, max = 50, message = "Room ID must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Room ID can only contain alphanumeric characters, hyphens, and underscores")
    private String roomId;

    public CreateRoomRequest() {
    }

    public CreateRoomRequest(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = (roomId != null) ? roomId.trim() : null;
    }
}
