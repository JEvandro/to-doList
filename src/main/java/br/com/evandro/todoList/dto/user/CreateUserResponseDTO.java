package br.com.evandro.todoList.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record CreateUserResponseDTO(


        @Schema(example = "admin")
        String name,

        @Schema(example = "admin")
        String username,

        @Schema(example = "admin@gmail.com")
        String email

){
}
