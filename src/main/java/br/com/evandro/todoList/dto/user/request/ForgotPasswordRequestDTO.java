package br.com.evandro.todoList.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequestDTO(

        @Schema(example = "admin@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @Email
        @NotBlank
        String email

) {
}
