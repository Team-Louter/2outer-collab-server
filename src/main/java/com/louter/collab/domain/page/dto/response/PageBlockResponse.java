package com.louter.collab.domain.page.dto.response;

import com.louter.collab.domain.page.entity.PageBlock;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PageBlockResponse {
    private Long blockId;
    private Long pageId;
    private Long parentBlockId;
    private String content;
    private String type;
    private Integer orderIndex;
    private Long authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PageBlockResponse from(PageBlock block) {
        return PageBlockResponse.builder()
                .blockId(block.getBlockId())
                .pageId(block.getPage().getPageId())
                .parentBlockId(block.getParentBlock() != null ? block.getParentBlock().getBlockId() : null)
                .content(block.getContent())
                .type(block.getType())
                .orderIndex(block.getOrderIndex())
                .authorId(block.getAuthor().getUserId())
                .createdAt(block.getCreatedAt())
                .updatedAt(block.getUpdatedAt())
                .build();
    }
}
