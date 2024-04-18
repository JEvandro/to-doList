package br.com.evandro.todoList.dto.task;

import java.time.LocalDateTime;

public record UpdateTaskResponseDTO(
        String description,
        LocalDateTime createdAt,
        LocalDateTime updateAt

) {
}
