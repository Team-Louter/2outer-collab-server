package com.louter.collab.domain.page.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PageCreateRequest {

    @NotNull
    private Long teamId;

    @NotNull
    private Long authorId;

    @NotNull
    private String title;
}
