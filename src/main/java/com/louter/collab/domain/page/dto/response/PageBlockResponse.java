package com.louter.collab.domain.page.dto.response;

import com.louter.collab.domain.page.entity.PageBlock;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PageBlockResponse {
    @NotNull
    private Long id;
    @NotNull
    private Long page;
    @NotNull
    private Long pageBlock;
    @NotNull
    private String content;
    @NotNull
    private String type;
    @NotNull
    private Integer orderIndex;
    @NotNull
    private LocalDateTime createdAt;
    @NotNull
    private LocalDateTime updatedAt;
    @NotNull
    private String author;

    public static PageBlockResponse from(PageBlock block) {
        return PageBlockResponse.builder()
                .id(block.getId())
                .page(block.getPage().getId())
                .pageBlock(block.getParent() != null ? block.getParent().getId() : null )
                .content(block.getContent())
                .type(block.getType())
                .orderIndex(block.getOrderIndex())
                .createdAt(block.getCreatedAt())
                .updatedAt(block.getUpdatedAt())
                .author(block.getAuthor().getUserName())
                .build();
    }
}
