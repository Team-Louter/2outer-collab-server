package com.louter.collab.domain.page.dto.response;

import com.louter.collab.domain.page.entity.Page;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class PageResponse {
    private Long pageId;
    private Long teamId;
    private String title;
    private Long authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PageBlockResponse> blocks;

    public static PageResponse from(Page page) {
        return PageResponse.builder()
                .pageId(page.getPageId())
                .teamId(page.getTeam().getTeamId())
                .title(page.getTitle())
                .authorId(page.getAuthor().getUserId())
                .createdAt(page.getCreatedAt())
                .updatedAt(page.getUpdatedAt())
                .blocks(page.getBlocks().stream()
                        .map(PageBlockResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
