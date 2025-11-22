package com.louter.collab.domain.page.dto.request;

import lombok.Getter;

@Getter
public class PageBlockEditRequest {
    private String content;

    private String type;

    private Integer orderIndex;
}
