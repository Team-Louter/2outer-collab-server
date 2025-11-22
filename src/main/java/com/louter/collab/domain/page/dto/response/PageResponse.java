package com.louter.collab.domain.page.dto.response;

import com.louter.collab.domain.page.entity.Page;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PageResponse {
    private Long id;

    private Long teamId;

    private String teamName;

    @NotNull
    private String title;



    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long authorId;
    private String authorName;

    public static PageResponse from(Page page) {
        return PageResponse.builder()
                .id(page.getId())
                .teamId(page.getTeam().getTeamId())
                .teamName(page.getTeam().getTeamName())
                .title(page.getTitle())
                .createdAt(page.getCreatedAt())
                .updatedAt(page.getUpdatedAt())
                .authorId(page.getAuthor().getUserId())
                .authorName(page.getAuthor().getUserName())
                .build();
    }
}
