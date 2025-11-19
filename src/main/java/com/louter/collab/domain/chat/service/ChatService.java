package com.louter.collab.domain.chat.service;

import com.louter.collab.domain.auth.entity.User;
import com.louter.collab.domain.chat.dto.request.ChatMessageRequest;
import com.louter.collab.domain.chat.dto.response.ChatMessageResponse;
import com.louter.collab.domain.chat.dto.response.ChatRoomResponse;

import com.louter.collab.domain.chat.entity.ChatRoom;

import java.util.List;

public interface ChatService {
    ChatMessageResponse saveMessage(Long chatRoomId, Long senderId, ChatMessageRequest request);
    void markMessageAsRead(Long chatRoomId, Long messageId, Long userId);
    ChatRoom createChatRoom(Long teamId, String channelName, User creator);
    List<ChatRoomResponse> getChatRoomsByTeam(Long teamId);
    List<ChatMessageResponse> getChatMessages(Long teamId, Long roomId, Long userId);
}
