package com.louter.collab.chat.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageRequest {
    private String message;
    private List<String> fileUrls;
}
