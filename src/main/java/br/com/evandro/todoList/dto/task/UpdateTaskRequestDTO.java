package br.com.evandro.todoList.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateTaskRequestDTO(

        @NotBlank
        @Schema(example = "Resolver a lista de matemática em grupo", requiredMode = Schema.RequiredMode.REQUIRED)
        String description

){
}
