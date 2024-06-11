package br.com.evandro.todoList.dto.user.request;

import java.util.UUID;

public record RefreshTokenRequestDTO(

        UUID refreshToken

) {
}
