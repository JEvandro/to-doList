package br.com.evandro.todoList.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UpdatePasswordRequestDTO(

        @Schema(example = "0123456789", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Length(min = 8, max = 100, message = "O campo [password] deve conter de 8 carateres até 100 caracateres no máximo")
        String oldPassword,

        @Schema(example = "newPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Length(min = 8, max = 100, message = "O campo [password] deve conter de 8 carateres até 100 caracateres no máximo")
        String newPassword,

        @Schema(example = "newPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Length(min = 8, max = 100, message = "O campo [password] deve conter de 8 carateres até 100 caracateres no máximo")
        String confirmPassword

) {
}
