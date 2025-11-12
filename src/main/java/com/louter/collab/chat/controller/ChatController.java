package com.louter.collab.chat.controller;

import com.louter.collab.auth.domain.User;
import com.louter.collab.chat.dto.request.ChatMessageRequest;
import com.louter.collab.chat.dto.request.MarkAsReadRequest;
import com.louter.collab.chat.dto.response.ChatMessageResponse;
import com.louter.collab.chat.dto.response.ChatRoomResponse;
import com.louter.collab.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/teams/{teamId}/chat/{roomId}/send")
    public void sendMessage(@DestinationVariable Long teamId,
                            @DestinationVariable Long roomId,
                            @Payload ChatMessageRequest chatMessageRequest,
                            SimpMessageHeaderAccessor accessor) {
        Long userId = Long.parseLong(accessor.getUser().getName());
        ChatMessageResponse response = chatService.saveMessage(roomId, userId, chatMessageRequest);
        messagingTemplate.convertAndSend("/sub/teams/" + teamId + "/chat/" + roomId, response);
    }

    @MessageMapping("/teams/{teamId}/chat/{roomId}/read")
    public void markAsRead(@DestinationVariable Long teamId,
                           @DestinationVariable Long roomId,
                           @Payload MarkAsReadRequest markAsReadRequest,
                           SimpMessageHeaderAccessor accessor) {
        Long userId = Long.parseLong(accessor.getUser().getName());
        chatService.markMessageAsRead(roomId, markAsReadRequest.getMessageId(), userId);
    }

    @GetMapping("/teams/{teamId}/chat/rooms")
    public ResponseEntity<List<ChatRoomResponse>> getChatRooms(@PathVariable Long teamId) {
        return ResponseEntity.ok(chatService.getChatRoomsByTeam(teamId));
    }
}
