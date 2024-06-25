package br.com.evandro.todoList.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record ResetPasswordRequestDTO(

        @Schema(example = "364762", minLength = 6, maxLength = 6, requiredMode = Schema.RequiredMode.REQUIRED)
        @Length(min = 6, max = 6, message = "O campo [code] deve conter exatamente 6 caracteres")
        @NotBlank
        String code,

        @Schema(example = "admin@123", minLength = 8, maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
        @Pattern(regexp = "\\S+", message = "O campo [password] não pode conter espaços")
        @Length(min = 8, max = 100, message = "O campo [password] deve conter de 8 caracteres até 100 caracteres no máximo")
        @NotBlank
        String password,

        @Schema(example = "admin@123", minLength = 8, maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
        @Pattern(regexp = "\\S+", message = "O campo [password] não pode conter espaços")
        @Length(min = 8, max = 100, message = "O campo [password] deve conter de 8 caracteres até 100 caracteres no máximo")
        @NotBlank
        String confirmPassword

) {
}
