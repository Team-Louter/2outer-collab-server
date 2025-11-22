package com.louter.collab.domain.page.dto.request;

import lombok.Getter;

@Getter
public class PageCreateRequest {

    private Long teamId;

    private Long authorId;

    private String title;
}
