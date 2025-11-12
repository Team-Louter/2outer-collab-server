package com.louter.collab.chat.service;

import com.louter.collab.auth.domain.User;
import com.louter.collab.chat.dto.request.ChatMessageRequest;
import com.louter.collab.chat.dto.response.ChatMessageResponse;
import com.louter.collab.chat.dto.response.ChatRoomResponse;

import com.louter.collab.chat.domain.ChatRoom;

import java.util.List;

public interface ChatService {
    ChatMessageResponse saveMessage(Long chatRoomId, Long senderId, ChatMessageRequest request);
    void markMessageAsRead(Long chatRoomId, Long messageId, Long userId);
    ChatRoom createChatRoom(Long teamId, String channelName, User creator);
    List<ChatRoomResponse> getChatRoomsByTeam(Long teamId);
}
