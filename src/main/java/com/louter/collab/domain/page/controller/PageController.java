package com.louter.collab.domain.page.controller;

import com.louter.collab.domain.page.dto.request.PageBlockUpdateRequest;
import com.louter.collab.domain.page.dto.request.PageCreateRequest;
import com.louter.collab.domain.page.dto.response.PageBlockResponse;
import com.louter.collab.domain.page.dto.response.PageResponse;
import com.louter.collab.domain.page.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;
    private final SimpMessagingTemplate messagingTemplate;

    // REST Endpoints

    @PostMapping("/teams/{teamId}/pages")
    public ResponseEntity<PageResponse> createPage(
            @PathVariable Long teamId,
            @RequestBody PageCreateRequest request,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(pageService.createPage(teamId, userId, request));
    }

    @GetMapping("/teams/{teamId}/pages")
    public ResponseEntity<List<PageResponse>> getPages(@PathVariable Long teamId) {
        return ResponseEntity.ok(pageService.getPagesByTeam(teamId));
    }

    @GetMapping("/teams/{teamId}/pages/{pageId}")
    public ResponseEntity<PageResponse> getPage(@PathVariable Long teamId, @PathVariable Long pageId) {
        return ResponseEntity.ok(pageService.getPage(pageId));
    }

    // WebSocket Endpoints

    @MessageMapping("/teams/{teamId}/pages/{pageId}/update")
    public void updateBlock(
            @DestinationVariable Long teamId,
            @DestinationVariable Long pageId,
            @Payload PageBlockUpdateRequest request,
            SimpMessageHeaderAccessor accessor) {
        
        Long userId = Long.parseLong(accessor.getUser().getName());
        List<PageBlockResponse> responses = pageService.updateBlock(pageId, userId, request);
        
        // Broadcast update to all subscribers of this page
        messagingTemplate.convertAndSend("/sub/teams/" + teamId + "/pages/" + pageId, responses);
    }

    @MessageMapping("/teams/{teamId}/pages/{pageId}/delete")
    public void deleteBlock(
            @DestinationVariable Long teamId,
            @DestinationVariable Long pageId,
            @Payload Long blockId,
            SimpMessageHeaderAccessor accessor) {
        
        Long userId = Long.parseLong(accessor.getUser().getName());
        pageService.deleteBlock(pageId, userId, blockId);
        
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("deletedBlockId", blockId);
        
        messagingTemplate.convertAndSend("/sub/teams/" + teamId + "/pages/" + pageId, response);
    }
}
