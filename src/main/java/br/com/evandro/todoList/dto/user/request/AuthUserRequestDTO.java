package br.com.evandro.todoList.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record AuthUserRequestDTO(
        @NotBlank
        @Length(max = 32, message = "O campo [username] contém até 32 caracteres no máximo")
        @Pattern(regexp = "\\S+\\w+", message = "O campo [username] não pode conter espaços em branco e caracteres que não sejam letras (a-z_A-z), números (0-9) ou sublinhado(_)")
        @Schema(example = "admin1_", maxLength = 32, requiredMode = Schema.RequiredMode.REQUIRED)
        String username,

        @NotBlank
        @Length(min = 8, max = 100, message = "O campo [password] deve conter de 8 carateres até 100 caracteres no máximo")
        @Pattern(regexp = "\\S+", message = "O campo [password] não pode conter espaços")
        @Schema(example = "0123456789", minLength = 8, maxLength = 100,requiredMode = Schema.RequiredMode.REQUIRED)
        String password
){
}
