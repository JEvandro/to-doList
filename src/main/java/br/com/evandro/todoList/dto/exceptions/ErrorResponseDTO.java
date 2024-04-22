package br.com.evandro.todoList.dto.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

public record ErrorResponseDTO(

        @Schema(example = "Mensagem de erro e da causa")
        String message
){
}
