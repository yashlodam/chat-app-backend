package com.chatapp.payload;

import com.chatapp.entity.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MessageRequest {

    @NotBlank(message = "Message content cannot be blank")
    @Size(max = 4000, message = "Message cannot exceed 4000 characters")
    private String content;

    @NotBlank(message = "Sender name cannot be blank")
    @Size(min = 1, max = 50, message = "Sender name must be between 1 and 50 characters")
    private String sender;

    private String roomId;

    private MessageType messageType = MessageType.CHAT;

    public MessageRequest() {
    }

    public MessageRequest(String sender, String content, String roomId, MessageType messageType) {
        this.sender = sender;
        this.content = content;
        this.roomId = roomId;
        this.messageType = (messageType != null) ? messageType : MessageType.CHAT;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = (messageType != null) ? messageType : MessageType.CHAT;
    }
}
