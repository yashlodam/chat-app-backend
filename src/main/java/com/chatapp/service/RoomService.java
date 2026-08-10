package com.chatapp.service;

import com.chatapp.entity.Room;
import com.chatapp.payload.CreateRoomRequest;
import com.chatapp.payload.MessageDto;
import com.chatapp.payload.PageResponse;
import com.chatapp.payload.RoomDto;

public interface RoomService {

    RoomDto createRoom(CreateRoomRequest request);

    RoomDto getRoomDtoByRoomId(String roomId);

    Room getRoomEntityByRoomId(String roomId);

    PageResponse<RoomDto> getAllRooms(int page, int size);

    PageResponse<MessageDto> getRoomMessages(String roomId, int page, int size);

    void deleteRoom(String roomId);
}
