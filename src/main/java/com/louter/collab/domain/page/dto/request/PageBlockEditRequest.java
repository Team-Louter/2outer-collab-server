package com.louter.collab.domain.page.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PageBlockEditRequest {
    @NotNull
    private String content;

    @NotNull
    private String type;

    @NotNull
    private Integer orderIndex;
}
