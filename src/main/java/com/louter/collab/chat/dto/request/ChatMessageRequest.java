package com.louter.collab.chat.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChatMessageRequest {
    private String message;
    private List<String> fileUrls;
}
