package br.com.evandro.todoList.dto.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HandlerExceptionMethodNotValidDTO{

    @Schema(example = "mensagem da causa de erro")
    private String message;

    @Schema(example = "campo responsável pelo erro")
    private String field;

}
