package br.com.evandro.todoList.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record UpdateTaskResponseDTO(

        @Schema(example = "Resolver a tarefa de matemática em grupo")
        String description,

        @Schema(example = "2024-04-22T16:59:10.811838")
        LocalDateTime createdAt,

        @Schema(example = "2024-05-20T10:59:10.811838")
        LocalDateTime updateAt

) {
}
