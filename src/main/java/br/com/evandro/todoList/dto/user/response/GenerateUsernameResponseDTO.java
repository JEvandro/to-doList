package br.com.evandro.todoList.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record GenerateUsernameResponseDTO(

        @Schema(example = "JoRgloReNuTsMATESItamEstrIADRAlu")
        String username

) {
}
