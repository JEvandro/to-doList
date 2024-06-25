package br.com.evandro.todoList.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record CreateUserResponseDTO(


        @Schema(example = "admin")
        String name,

        @Schema(example = "admin1_")
        String username,

        @Schema(example = "admin@gmail.com")
        String email,

        @Schema(example = "PENDENT")
        String status,

        @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
        String access_token,

        @Schema(example = "6b0b2199-4f12-4929-bdef-d332907932ae")
        UUID refreshToken

){
}
