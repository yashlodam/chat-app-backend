package com.chatapp.service;

import com.chatapp.payload.MessageDto;
import com.chatapp.payload.MessageRequest;

public interface ChatService {

    MessageDto sendMessage(String roomId, MessageRequest request);
}
