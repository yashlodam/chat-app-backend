package com.chatapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.entity.Message;
import com.chatapp.entity.Room;
import com.chatapp.repository.RoomRepository;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

	@Autowired
	private RoomRepository roomRepository;
	
	
	//create room
	
	@PostMapping
	public ResponseEntity<?> createRoom(@RequestBody String roomId) {

	    if (roomRepository.findByRoomId(roomId) != null) {

	        return new ResponseEntity<>("Room already exists", HttpStatus.BAD_REQUEST);

	    }

	    Room room = new Room();
	    room.setRoomId(roomId);

	    Room savedRoom = roomRepository.save(room);

	    return new ResponseEntity<>(savedRoom, HttpStatus.CREATED);
	}
	
	
	//get room
	
	@GetMapping("/{roomId}")
	public ResponseEntity<?> joinRoom(@PathVariable String roomId){
		
		Room room = roomRepository.findByRoomId(roomId);
		
		if(room == null) {
			
			return new ResponseEntity<>("Room not exits or null",HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(room,HttpStatus.OK);
		
	}
	
	
	
	
	
	
	//get messages of room
	
	@GetMapping("/{roomId}/messages")
	public ResponseEntity<?> getMessages(
	        @PathVariable String roomId,
	        @RequestParam(value = "page", defaultValue = "0", required = false) int page,
	        @RequestParam(value = "size", defaultValue = "20", required = false) int size) {

	    Room room = roomRepository.findByRoomId(roomId);

	    if (room == null) {
	        return new ResponseEntity<>(
	                "Room is null or blank",
	                HttpStatus.BAD_REQUEST
	        );
	    }

	    // Get messages from this room
	    List<Message> messages = room.getMessages();
	    
	    int start = Math.max(0, messages.size()-(page+1)*size);
	    
	    int end = Math.min(messages.size(), start+size);
	    
	  List<Message> paginatedMessages =  messages.subList(start, end);

	    return new ResponseEntity<>(paginatedMessages, HttpStatus.OK);
	}
	
}
