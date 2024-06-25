package br.com.evandro.todoList.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record UpdateProfileUserRequestDTO(

        @Schema(example = "admin dois", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @NotNull(message = "O campo [name] não pode ser nulo")
        @Pattern(regexp = "^$|^[?!.*\\s{2}][a-zA-Zà-úÀ-Ú\\s]+$", message = "O campo [name] deve ser válido")
        @Length(max = 80, message = "O campo [name] deve conter até 80 caracteres no máximo")
        String name,

        @Schema(example = "admin2", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @NotNull(message = "O campo [username] não pode ser nulo")
        @Pattern(regexp = "^$|^[\\S+\\w+]+$", message = "O campo [username] não pode conter espaços em branco e caracteres que não sejam letras (a-z_A-z), números (0-9) ou sublinhado(_)")
        @Length(max = 32, message = "O campo [username] deve conter até 32 caracteres no máximo")
        String username,

        @Schema(example = "admin2@gmail.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Length(max = 254, message = "O campo [Email] deve conter até 254 caracteres no máximo")
        @NotNull(message = "O campo [email] não pode ser nulo")
        @Pattern(regexp = "(^$|^[a-zA-Z0-9]([a-zA-Z0-9._-]*[a-zA-Z0-9])?@[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,6})+$)", message = "O campo [Email] deve conter um e-mail válido")
        String email

){
}
