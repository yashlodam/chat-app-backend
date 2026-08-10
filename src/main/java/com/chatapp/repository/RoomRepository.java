package com.chatapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatapp.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

	Room findByRoomId(String roomId);
}
