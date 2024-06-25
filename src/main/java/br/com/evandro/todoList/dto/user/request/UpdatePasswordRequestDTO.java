package br.com.evandro.todoList.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record UpdatePasswordRequestDTO(

        @Schema(example = "0123456789", minLength = 8, maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Pattern(regexp = "\\S+", message = "O campo [password] não pode conter espaços")
        @Length(min = 8, max = 100, message = "O campo [password] deve conter de 8 caracteres até 100 caracteres no máximo")
        String oldPassword,

        @Schema(example = "newPassword123", minLength = 8, maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Pattern(regexp = "\\S+", message = "O campo [password] não pode conter espaços")
        @Length(min = 8, max = 100, message = "O campo [password] deve conter de 8 caracteres até 100 caracteres no máximo")
        String newPassword,

        @Schema(example = "newPassword123", minLength = 8, maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Pattern(regexp = "\\S+", message = "O campo [password] não pode conter espaços")
        @Length(min = 8, max = 100, message = "O campo [password] deve conter de 8 caracteres até 100 caracteres no máximo")
        String confirmPassword

) {
}
