package com.louter.collab.domain.page.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PageUpdateRequest {
    @NotNull
    private String title;
}
