package com.louter.collab.domain.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ChatMessageResponse {
    private final Long messageId;
    private final Long senderId;
    private final String senderName;
    private final String message;
    private final LocalDateTime createdAt;
    private final List<String> fileUrls;

    @Builder
    public ChatMessageResponse(Long messageId, Long senderId, String senderName, String message, LocalDateTime createdAt, List<String> fileUrls) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.message = message;
        this.createdAt = createdAt;
        this.fileUrls = fileUrls;
    }
}
