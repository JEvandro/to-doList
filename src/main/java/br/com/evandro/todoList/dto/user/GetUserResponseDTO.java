package br.com.evandro.todoList.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetUserResponseDTO(

        @Schema(example = "738dc9da-d507-4c38-b067-fb0072c00ea7")
        UUID id,

        @Schema(example = "admin")
        String name,

        @Schema(example = "admin")
        String username,

        @Schema(example = "admin@gmail.com")
        String email,

        @Schema(example = "2024-04-22T16:59:10.811838")
        LocalDateTime createdAt

){
}
