package br.com.evandro.todoList.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserConfirmationResponseDTO(

        @Schema(example = "admin")
        String username,

        @Schema(example = "ACTIVE")
        String status

) {

}
