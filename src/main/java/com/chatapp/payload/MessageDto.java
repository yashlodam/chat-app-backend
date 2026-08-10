package com.chatapp.payload;

import java.time.LocalDateTime;
import com.chatapp.entity.MessageType;

public class MessageDto {

    private Long id;
    private String sender;
    private String content;
    private LocalDateTime timeStamp;
    private String roomId;
    private MessageType messageType;

    public MessageDto() {
    }

    public MessageDto(Long id, String sender, String content, LocalDateTime timeStamp, String roomId, MessageType messageType) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.timeStamp = timeStamp;
        this.roomId = roomId;
        this.messageType = messageType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
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
        this.messageType = messageType;
    }
}
