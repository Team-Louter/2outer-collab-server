package com.louter.collab.page.dto.response;

import com.louter.collab.page.domain.Page;
import com.louter.collab.page.domain.PageBlock;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageBlockResponse {
    private Long id;
    private String type;
    private String content;
    private Long orderIndex;

    public static PageBlockResponse from(PageBlock pageBlock) {
        return PageBlockResponse.builder()
                .id(pageBlock.getId())
                .type(pageBlock.getType())
                .content(pageBlock.getContent())
                .orderIndex(pageBlock.getOrderIndex())
                .build();
    }
}
