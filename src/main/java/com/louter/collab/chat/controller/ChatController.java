package com.louter.collab.chat.controller;

import com.louter.collab.auth.domain.User;
import com.louter.collab.chat.dto.request.ChatMessageRequest;
import com.louter.collab.chat.dto.request.MarkAsReadRequest;
import com.louter.collab.chat.dto.response.ChatMessageResponse;
import com.louter.collab.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/rooms/{roomId}/send")
    public void sendMessage(@DestinationVariable Long roomId,
                            @Payload ChatMessageRequest chatMessageRequest,
                            SimpMessageHeaderAccessor accessor) {
        Long userId = Long.parseLong(accessor.getUser().getName());
        ChatMessageResponse response = chatService.saveMessage(roomId, userId, chatMessageRequest);
        messagingTemplate.convertAndSend("/sub/chat/rooms/" + roomId, response);
    }

    @MessageMapping("/chat/rooms/{roomId}/read")
    public void markAsRead(@DestinationVariable Long roomId,
                           @Payload MarkAsReadRequest markAsReadRequest,
                           SimpMessageHeaderAccessor accessor) {
        Long userId = Long.parseLong(accessor.getUser().getName());
        chatService.markMessageAsRead(roomId, markAsReadRequest.getMessageId(), userId);
    }
}
