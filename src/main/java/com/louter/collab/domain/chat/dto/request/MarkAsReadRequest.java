package com.louter.collab.domain.chat.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MarkAsReadRequest {
    private Long messageId;
}
