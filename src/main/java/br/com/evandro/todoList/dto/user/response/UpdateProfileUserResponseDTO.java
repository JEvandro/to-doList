package br.com.evandro.todoList.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record UpdateProfileUserResponseDTO(

        @Schema(example = "admin2")
        String name,

        @Schema(example = "admin2")
        String username,

        @Schema(example = "admin2@gmail.com")
        String email,

        @Schema(example = "2024-04-22T16:59:10.811838")
        LocalDateTime createdAt,

        @Schema(example = "2024-04-23T10:00:10.811838")
        LocalDateTime updateAt

) {
}
