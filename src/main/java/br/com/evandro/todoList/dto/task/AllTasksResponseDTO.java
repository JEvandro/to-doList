package br.com.evandro.todoList.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record AllTasksResponseDTO(

        @Schema(example = "738dc9da-d507-4c38-b067-fb0072c00ea7")
        UUID id,

        @Schema(example = "Resolver a lista de matemática")
        String description,

        @Schema(example = "true")
        boolean isCompleted,

        @Schema(example = "2024-04-22T16:59:10.811838")
        LocalDateTime createdAt,

        @Schema(example = "2024-05-20T10:59:10.811838")
        LocalDateTime updateAt
) {
}
