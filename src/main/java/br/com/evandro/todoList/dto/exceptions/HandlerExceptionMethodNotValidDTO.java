package br.com.evandro.todoList.dto.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HandlerExceptionMethodNotValidDTO{

    private String message;
    private String field;

}
