package br.com.evandro.todoList.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record UserResponseDTO(

        @Schema(example = "Jose Evandro")
        String name,

        @Schema(example = "joseevandro")
        String username,

        @Schema(example = "jose@gmail.com")
        String email,

        @Schema(example = "2024-04-22T16:59:10.811838")
        LocalDateTime createdAt

){
}
