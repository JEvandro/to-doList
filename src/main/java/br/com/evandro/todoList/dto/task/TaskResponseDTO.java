package br.com.evandro.todoList.dto.task;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponseDTO(
        String description,
        LocalDateTime createdAt,
        UUID userId
) {
}
