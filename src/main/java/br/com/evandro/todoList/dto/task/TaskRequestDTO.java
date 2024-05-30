package br.com.evandro.todoList.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record TaskRequestDTO(
        @NotBlank
        @Length(min = 1, max = 150, message = "O campo [description] deve conter no entre 1 caracter até no máximo 150 caracteres")
        @Schema(example = "Resolver a lista de matemática", requiredMode = Schema.RequiredMode.REQUIRED)
        String description
){
}
