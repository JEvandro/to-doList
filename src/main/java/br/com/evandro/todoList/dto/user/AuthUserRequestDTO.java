package br.com.evandro.todoList.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record AuthUserRequestDTO(
        @NotBlank
        @Pattern(regexp = "\\S+", message = "O campo [username] não pode conter espaço em branco")
        String username,

        @NotBlank
        @Length(min = 8, max = 100, message = "O campo [password] deve conter de 8 carateres até 100 caracateres no máximo")
        String password
){
}
