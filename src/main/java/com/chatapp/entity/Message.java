package com.chatapp.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "messages", indexes = {
    @Index(name = "idx_messages_room_id", columnList = "room_id"),
    @Index(name = "idx_messages_room_timestamp", columnList = "room_id, timeStamp")
})
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String sender;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime timeStamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MessageType messageType = MessageType.CHAT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @JsonIgnore
    private Room room;

    public Message() {
    }

    public Message(String sender, String content, Room room) {
        this.sender = sender;
        this.content = content;
        this.room = room;
        this.timeStamp = LocalDateTime.now();
        this.messageType = MessageType.CHAT;
    }

    public Message(String sender, String content, Room room, MessageType messageType) {
        this.sender = sender;
        this.content = content;
        this.room = room;
        this.timeStamp = LocalDateTime.now();
        this.messageType = (messageType != null) ? messageType : MessageType.CHAT;
    }

    public Message(Long id, String sender, String content, LocalDateTime timeStamp, Room room, MessageType messageType) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.timeStamp = timeStamp;
        this.room = room;
        this.messageType = (messageType != null) ? messageType : MessageType.CHAT;
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

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = (messageType != null) ? messageType : MessageType.CHAT;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}