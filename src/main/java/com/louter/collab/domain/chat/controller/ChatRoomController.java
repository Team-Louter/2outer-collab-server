package com.louter.collab.domain.chat.controller;

import com.louter.collab.domain.auth.entity.User;
import com.louter.collab.domain.chat.entity.ChatRoom;
import com.louter.collab.domain.chat.dto.request.ChatRoomCreateRequest;
import com.louter.collab.domain.chat.dto.response.ChatRoomResponse;
import com.louter.collab.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/teams/{teamId}/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatRoomResponse> createChatRoom(
            @PathVariable Long teamId,
            @RequestBody ChatRoomCreateRequest request,
            @AuthenticationPrincipal User user) {

        ChatRoom createdChatRoom = chatService.createChatRoom(teamId, request.getChannelName(), user);
        ChatRoomResponse response = new ChatRoomResponse(createdChatRoom);

        URI location = URI.create(String.format("/api/teams/%d/chatrooms/%d", teamId, createdChatRoom.getId()));
        return ResponseEntity.created(location).body(response);
    }
}
