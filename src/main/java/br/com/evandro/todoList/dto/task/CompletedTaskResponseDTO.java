package br.com.evandro.todoList.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;

public record CompletedTaskResponseDTO(

        @Schema(example = "Resolver a lista de matemática")
        String description,

        @Schema(example = "true")
        boolean isCompleted

){
}
