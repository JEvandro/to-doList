package br.com.evandro.todoList.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record GetOtherUserResponseDTO(

        @Schema(example = "admin")
        String name,

        @Schema(example = "admin1_")
        String username,

        @Schema(example = "admin@gmail.com")
        String email,

        @Schema(example = "2024-04-22T16:59:10.811838")
        LocalDateTime createdAt

) {
}
