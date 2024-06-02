package br.com.evandro.todoList.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.UUID;

public record CreateUserResponseDTO(


        @Schema(example = "admin")
        String name,

        @Schema(example = "admin")
        String username,

        @Schema(example = "admin@gmail.com")
        String email,

        String access_token,
        UUID refreshToken

){
}
