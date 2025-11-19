package com.louter.collab.page.dto.response;

import com.louter.collab.page.domain.Page;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageResponse {
    private Long id;
    private String title;

    public static PageResponse from(Page page) {
        return PageResponse.builder()
                .id(page.getId())
                .title(page.getTitle())
                .build();
    }
}
