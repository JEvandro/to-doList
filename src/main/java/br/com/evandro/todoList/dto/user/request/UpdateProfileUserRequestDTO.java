package br.com.evandro.todoList.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UpdateProfileUserRequestDTO(

        @Schema(example = "admin2", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @NotNull(message = "O campo [name] não pode ser nulo")
        @Length(max = 80, message = "O campo [name] deve conter até 80 caracateres no máximo")
        String name,

        @Schema(example = "admin2", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @NotNull(message = "O campo [username] não pode ser nulo")
        @Length(max = 32, message = "O campo [username] deve conter até 32 caracateres no máximo")
        String username,

        @Schema(example = "admin2@gmail.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @NotNull(message = "O campo [email] não pode ser nulo")
        @Email(message = "O campo [Email] deve conter um e-mail válido")
        String email

){
}
