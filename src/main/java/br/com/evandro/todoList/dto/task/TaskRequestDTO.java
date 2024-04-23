package br.com.evandro.todoList.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record TaskRequestDTO(
        @NotBlank
        @Schema(example = "Resolver a lista de matemática", requiredMode = Schema.RequiredMode.REQUIRED)
        String description
){
}
