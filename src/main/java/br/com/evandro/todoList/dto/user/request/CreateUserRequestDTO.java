package br.com.evandro.todoList.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record CreateUserRequestDTO(
        @NotBlank
        @Length(min = 3, max = 80, message = "O campo [name] deve conter de 3 caracteres até 80 caracteres no máximo")
        @Pattern(regexp = "^(?!.*\\s{2})[a-zA-Zà-úÀ-Ú\\s]+$", message = "O campo [name] deve ser válido")
        @Schema(example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @NotBlank
        @Pattern(regexp = "\\S+\\w+", message = "O campo [username] não pode conter espaços em branco e caracteres que não sejam letras (a-z_A-z), números (0-9) ou sublinhado(_)")
        @Length(max = 32, message = "O campo [username] deve conter até 32 caracteres no máximo")
        @Schema(example = "admin1_", maxLength = 254, requiredMode = Schema.RequiredMode.REQUIRED)
        String username,

        @NotBlank
        @Length(max = 254, message = "O campo [Email] contém até 254 caracteres no máximo")
        @Pattern(regexp = "^[a-zA-Z0-9]([a-zA-Z0-9._-]*[a-zA-Z0-9])?@[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,6})+$", message = "O campo [Email] deve conter um e-mail válido")
        @Schema(example = "admin@gmail.com", maxLength = 254, requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @NotBlank
        @Length(min = 8, max = 100, message = "O campo [password] deve conter de 8 carateres até 100 caracteres no máximo")
        @Pattern(regexp = "\\S+", message = "O campo [password] não pode conter espaços")
        @Schema(example = "0123456789", minLength = 8, maxLength = 100,requiredMode = Schema.RequiredMode.REQUIRED)
        String password

){
}
