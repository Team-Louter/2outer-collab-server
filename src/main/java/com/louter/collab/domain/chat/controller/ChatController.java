package com.louter.collab.domain.chat.controller;

import com.louter.collab.domain.chat.dto.request.ChatMessageRequest;
import com.louter.collab.domain.chat.dto.request.MarkAsReadRequest;
import com.louter.collab.domain.chat.dto.response.ChatMessageResponse;
import com.louter.collab.domain.chat.dto.response.ChatRoomResponse;
import com.louter.collab.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;




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

    @PostMapping("/teams/{teamId}/chat/{roomId}/messages")
    public ResponseEntity<ChatMessageResponse> sendChatMessage(
            @PathVariable Long teamId,
            @PathVariable Long roomId,
            @RequestBody ChatMessageRequest chatMessageRequest,
            Authentication authentication) {
        
        Long userId = Long.parseLong(authentication.getName());
        
        ChatMessageResponse response = chatService.saveMessage(roomId, userId, chatMessageRequest);
        
        // WebSocket으로 실시간 전송
        messagingTemplate.convertAndSend("/sub/teams/" + teamId + "/chat/" + roomId, response);
        
        return ResponseEntity.ok(response);
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

    @GetMapping("/teams/{teamId}/chat/{roomId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getChatMessages(
            @PathVariable Long teamId,
            @PathVariable Long roomId,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(chatService.getChatMessages(teamId, roomId, userId));
    }
}
