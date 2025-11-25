package com.louter.collab.domain.todo.service.impl;

import com.louter.collab.domain.todo.dto.request.TodoCreateRequest;
import com.louter.collab.domain.todo.dto.request.TodoUpdateRequest;
import com.louter.collab.domain.todo.dto.response.TodoResponse;
import com.louter.collab.domain.todo.entity.Todo;
import com.louter.collab.domain.todo.repository.TodoRepository;
import com.louter.collab.domain.todo.service.TodoService;
import com.louter.collab.global.common.exception.TodoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    @Override
    @Transactional
    public TodoResponse createTodo(TodoCreateRequest request) {
        Todo todo = Todo.builder()
                .title(request.getTitle())
                .done(false)
                .build();

        return TodoResponse.from(todoRepository.save(todo));
    }

    @Override
    @Transactional
    public TodoResponse updateTodo(Long todoId, TodoUpdateRequest request) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoNotFoundException("해당 Todo를 찾을 수 없습니다 : " + todoId));

        todo.setTitle(request.getTitle());
        todo.setDone(request.isDone());

        return TodoResponse.from(todo);
    }

    @Override
    @Transactional
    public void deleteTodo(Long todoId) {
        todoRepository.deleteById(todoId);
    }

    @Override
    public List<TodoResponse> getTodos() {
        return todoRepository.findAll().stream()
                .map(TodoResponse::from)
                .collect(Collectors.toList());
    }
}