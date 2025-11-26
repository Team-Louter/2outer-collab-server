package com.louter.collab.domain.todo.dto.response;

import com.louter.collab.domain.todo.entity.Todo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TodoResponse {
    private Long todoId;
    private String title;
    private boolean done;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TodoResponse from(Todo todo) {
        return TodoResponse.builder()
                .todoId(todo.getTodoId())
                .title(todo.getTitle())
                .done(todo.isDone())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .build();
    }
}
