package br.com.evandro.todoList.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record RefreshTokenRequestDTO(

        @Schema(example = "738dc9da-d507-4c38-b067-fb0072c00ea7", requiredMode = Schema.RequiredMode.REQUIRED)
        UUID refreshToken

) {
}
