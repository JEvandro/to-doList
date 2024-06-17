package br.com.evandro.todoList.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record RefreshTokenResponseDTO(

        @Schema(example = "738dc9da-d507-4c38-b067-fb0072c00ea7")
        String access_token,

        @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
        UUID refresh_token
) {
}
