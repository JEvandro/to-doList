package br.com.evandro.todoList.dto.task;

import java.time.LocalDateTime;
import java.util.UUID;

public record AllTasksResponseDTO(
        UUID id,
        String description,
        boolean isCompleted,
        LocalDateTime createdAt,
        LocalDateTime updateAt
) {
}
