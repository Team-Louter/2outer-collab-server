package com.louter.collab.domain.todo.controller;

import com.louter.collab.domain.todo.dto.request.TodoCreateRequest;
import com.louter.collab.domain.todo.dto.request.TodoUpdateRequest;
import com.louter.collab.domain.todo.dto.response.TodoResponse;
import com.louter.collab.domain.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public TodoResponse createTodo(@RequestBody TodoCreateRequest request) {
        return todoService.createTodo(request);
    }

    @GetMapping
    public List<TodoResponse> getTodos() {
        return todoService.getTodos();
    }

    @PutMapping("/{todoId}")
    public TodoResponse updateTodo(
            @PathVariable Long todoId,
            @RequestBody TodoUpdateRequest request
    ) {
        return todoService.updateTodo(todoId, request);
    }


    @DeleteMapping("/{todoId}")
    public void deleteTodo(@PathVariable Long todoId) {
        todoService.deleteTodo(todoId);
    }
}
