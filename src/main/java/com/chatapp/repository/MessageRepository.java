package com.chatapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatapp.entity.Message;
import com.chatapp.entity.Room;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByRoom_RoomIdOrderByTimeStampAsc(String roomId, Pageable pageable);

    Page<Message> findByRoom_RoomIdOrderByTimeStampDesc(String roomId, Pageable pageable);

    Page<Message> findByRoomOrderByTimeStampAsc(Room room, Pageable pageable);

    long countByRoom_RoomId(String roomId);

    long countByRoom(Room room);
}
