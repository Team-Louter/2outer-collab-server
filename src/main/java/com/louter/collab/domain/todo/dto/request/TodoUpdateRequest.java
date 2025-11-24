package com.louter.collab.domain.todo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TodoUpdateRequest {
    @NotNull
    private String title;
    @NotNull
    private Boolean done;
}
