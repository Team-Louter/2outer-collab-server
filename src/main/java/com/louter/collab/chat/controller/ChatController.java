package com.louter.collab.chat.controller;

import com.louter.collab.auth.domain.User;
import com.louter.collab.chat.dto.request.ChatMessageRequest;
import com.louter.collab.chat.dto.request.MarkAsReadRequest;
import com.louter.collab.chat.dto.response.ChatMessageResponse;
import com.louter.collab.chat.dto.response.ChatRoomResponse;
import com.louter.collab.chat.service.ChatService;
import com.louter.collab.common.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final FileStorageService fileStorageService;

    private String uploadFile(MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/download/")
                .path(fileName)
                .toUriString();
    }

    @MessageMapping("/teams/{teamId}/chat/{roomId}/send")
    public void sendMessage(@DestinationVariable Long teamId,
                            @DestinationVariable Long roomId,
                            @Payload ChatMessageRequest chatMessageRequest,
                            SimpMessageHeaderAccessor accessor) {
        Long userId = Long.parseLong(accessor.getUser().getName());
        ChatMessageResponse response = chatService.saveMessage(roomId, userId, chatMessageRequest);
        messagingTemplate.convertAndSend("/sub/teams/" + teamId + "/chat/" + roomId, response);
    }

    @PostMapping(value = "/teams/{teamId}/chat/{roomId}/messages", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ChatMessageResponse> sendChatMessage(
            @PathVariable Long teamId,
            @PathVariable Long roomId,
            @RequestPart("request") ChatMessageRequest chatMessageRequest,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            Authentication authentication) {
        
        Long userId = Long.parseLong(authentication.getName());
        
        List<String> fileUrls = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                fileUrls.add(uploadFile(file));
            }
        }
        
        if (chatMessageRequest.getFileUrls() == null) {
            chatMessageRequest.setFileUrls(fileUrls);
        } else {
            chatMessageRequest.getFileUrls().addAll(fileUrls);
        }

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
