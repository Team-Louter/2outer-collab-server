package com.louter.collab.page.dto.response;

import com.louter.collab.page.domain.PageBlock;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PageBlockResponse {
    private Long id;
    private Long page;
    private Long pageBlock;
    private String content;
    private String type;
    private Long orderIndex;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String author;

    public static PageBlockResponse from(PageBlock block) {
        return PageBlockResponse.builder()
                .id(block.getId())
                .page(block.getPage().getId())
                .pageBlock(block.getPageBlock() != null ? block.getPageBlock().getId() : null )
                .content(block.getContent())
                .type(block.getType())
                .orderIndex(block.getOrderIndex())
                .createdAt(block.getCreatedAt())
                .updatedAt(block.getUpdatedAt())
                .author(block.getAuthor().getUserName())
                .build();
    }
}
