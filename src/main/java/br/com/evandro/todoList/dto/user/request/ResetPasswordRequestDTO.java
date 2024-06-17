package br.com.evandro.todoList.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record ResetPasswordRequestDTO(

        @Schema(example = "364762", requiredMode = Schema.RequiredMode.REQUIRED)
        @Length(min = 6, max = 6, message = "O campo [code] deve conter exatamente 6 carateres")
        @NotBlank
        String code,

        @Schema(example = "admin@123", requiredMode = Schema.RequiredMode.REQUIRED)
        @Length(min = 8, max = 100, message = "O campo [password] deve conter de 8 carateres até 100 caracateres no máximo")
        @NotBlank
        String password,

        @Schema(example = "admin@123", requiredMode = Schema.RequiredMode.REQUIRED)
        @Length(min = 8, max = 100, message = "O campo [password] deve conter de 8 carateres até 100 caracateres no máximo")
        @NotBlank
        String confirmPassword

) {
}
