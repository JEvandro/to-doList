package br.com.evandro.todoList.dto.user;

import java.util.UUID;

public record RefreshTokenResponseDTO(

        String access_token,
        UUID refresh_token
) {
}
