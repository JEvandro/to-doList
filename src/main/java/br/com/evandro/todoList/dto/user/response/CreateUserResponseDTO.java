package br.com.evandro.todoList.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record CreateUserResponseDTO(


        @Schema(example = "admin")
        String name,

        @Schema(example = "admin")
        String username,

        @Schema(example = "admin@gmail.com")
        String email,
        String status,
        String access_token,
        UUID refreshToken

){
}
