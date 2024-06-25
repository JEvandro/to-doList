package br.com.evandro.todoList.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record ForgotPasswordRequestDTO(

        @Schema(example = "admin@gmail.com", maxLength = 254, requiredMode = Schema.RequiredMode.REQUIRED)
        @Length(max = 254, message = "O campo [Email] contém até 254 caracteres no máximo")
        @Pattern(regexp = "^[a-zA-Z0-9]([a-zA-Z0-9._-]*[a-zA-Z0-9])?@[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,6})+$", message = "O campo [Email] deve conter um e-mail válido")
        @NotBlank
        String email

) {
}
