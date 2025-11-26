package com.louter.collab.domain.notice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class NoticeUpdateRequest {
    @NotNull
    private String title;

    @NotNull
    private String content;
}
