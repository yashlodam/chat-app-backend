package com.chatapp.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chatapp.entity.Message;
import com.chatapp.entity.Room;
import com.chatapp.exception.ResourceAlreadyExistsException;
import com.chatapp.exception.ResourceNotFoundException;
import com.chatapp.payload.CreateRoomRequest;
import com.chatapp.payload.MessageDto;
import com.chatapp.payload.PageResponse;
import com.chatapp.payload.RoomDto;
import com.chatapp.repository.MessageRepository;
import com.chatapp.repository.RoomRepository;
import com.chatapp.service.RoomService;

@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;

    public RoomServiceImpl(RoomRepository roomRepository, MessageRepository messageRepository) {
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    @Transactional
    public RoomDto createRoom(CreateRoomRequest request) {
        String roomId = request.getRoomId().trim();
        logger.info("Creating room with ID: {}", roomId);

        if (roomRepository.existsByRoomId(roomId)) {
            logger.warn("Room already exists with ID: {}", roomId);
            throw new ResourceAlreadyExistsException("Room", "roomId", roomId);
        }

        Room room = new Room(roomId);
        Room savedRoom = roomRepository.save(room);

        return mapToRoomDto(savedRoom, 0L);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomDto getRoomDtoByRoomId(String roomId) {
        Room room = getRoomEntityByRoomId(roomId);
        long messageCount = messageRepository.countByRoom_RoomId(roomId);
        return mapToRoomDto(room, messageCount);
    }

    @Override
    @Transactional(readOnly = true)
    public Room getRoomEntityByRoomId(String roomId) {
        return roomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "roomId", roomId));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<RoomDto> getAllRooms(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Room> roomsPage = roomRepository.findAll(pageable);

        List<RoomDto> dtos = roomsPage.getContent().stream()
                .map(room -> {
                    long count = messageRepository.countByRoom_RoomId(room.getRoomId());
                    return mapToRoomDto(room, count);
                })
                .collect(Collectors.toList());

        return new PageResponse<>(
                dtos,
                roomsPage.getNumber(),
                roomsPage.getSize(),
                roomsPage.getTotalElements(),
                roomsPage.getTotalPages(),
                roomsPage.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MessageDto> getRoomMessages(String roomId, int page, int size) {
        if (!roomRepository.existsByRoomId(roomId)) {
            throw new ResourceNotFoundException("Room", "roomId", roomId);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "timeStamp"));
        Page<Message> messagePage = messageRepository.findByRoom_RoomIdOrderByTimeStampAsc(roomId, pageable);

        List<MessageDto> messageDtos = messagePage.getContent().stream()
                .map(this::mapToMessageDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                messageDtos,
                messagePage.getNumber(),
                messagePage.getSize(),
                messagePage.getTotalElements(),
                messagePage.getTotalPages(),
                messagePage.isLast()
        );
    }

    @Override
    @Transactional
    public void deleteRoom(String roomId) {
        Room room = getRoomEntityByRoomId(roomId);
        logger.info("Deleting room: {}", roomId);
        roomRepository.delete(room);
    }

    private RoomDto mapToRoomDto(Room room, long totalMessages) {
        return new RoomDto(room.getId(), room.getRoomId(), room.getCreatedAt(), totalMessages);
    }

    private MessageDto mapToMessageDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getSender(),
                message.getContent(),
                message.getTimeStamp(),
                message.getRoom() != null ? message.getRoom().getRoomId() : null,
                message.getMessageType()
        );
    }
}
