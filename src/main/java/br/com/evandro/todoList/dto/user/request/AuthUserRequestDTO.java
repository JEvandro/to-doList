package br.com.evandro.todoList.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record AuthUserRequestDTO(
        @NotBlank
        @Pattern(regexp = "\\S+", message = "O campo [username] não pode conter espaço em branco")
        @Schema(example = "admin",requiredMode = Schema.RequiredMode.REQUIRED)
        String username,

        @NotBlank
        @Length(min = 8, max = 100, message = "O campo [password] deve conter de 8 carateres até 100 caracateres no máximo")
        @Schema(example = "0123456789", minLength = 8, maxLength = 100,requiredMode = Schema.RequiredMode.REQUIRED)
        String password
){
}
