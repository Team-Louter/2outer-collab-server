package com.louter.collab.page.dto.request;

import lombok.Getter;

@Getter
public class PageBlockEditRequest {
    private String content;

    private String type;

    private Long orderIndex;
}
