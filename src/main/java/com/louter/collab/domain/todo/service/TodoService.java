package com.louter.collab.domain.todo.service;

import com.louter.collab.domain.todo.dto.request.TodoCreateRequest;
import com.louter.collab.domain.todo.dto.request.TodoUpdateRequest;
import com.louter.collab.domain.todo.dto.response.TodoResponse;

import java.util.List;

public interface TodoService {
    TodoResponse createTodo(TodoCreateRequest request);

    TodoResponse updateTodo(Long todoId, TodoUpdateRequest request);

    void deleteTodo(Long todoId);

    List<TodoResponse> getTodos();
}
