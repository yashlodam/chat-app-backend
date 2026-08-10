package com.chatapp.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.entity.Message;
import com.chatapp.entity.Room;
import com.chatapp.payload.MessageRequest;
import com.chatapp.repository.RoomRepository;

@RestController
@CrossOrigin("*")
public class ChatController {

	@Autowired
	private RoomRepository roomRepository;
	
	
	@MessageMapping("/sendMessage/{roomId}") // send message app/sendMessage/roomid
	@SendTo("/topic/room/{roomId}") // subscribe to this 
	public Message sendMessage(
	        @DestinationVariable String roomId,
	        MessageRequest request
	) {

	    Room room = roomRepository.findByRoomId(roomId);

	    if (room == null) {
	        throw new RuntimeException("Room not found");
	    }

	    Message message = new Message();

	    message.setContent(request.getContent());
	    message.setSender(request.getSender());
	    message.setTimeStamp(LocalDateTime.now());

	    message.setRoom(room);

	    room.getMessages().add(message);

	    roomRepository.save(room);

	    return message;
	}
}
