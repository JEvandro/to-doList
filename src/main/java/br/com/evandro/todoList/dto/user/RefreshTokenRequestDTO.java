package br.com.evandro.todoList.dto.user;

import java.util.UUID;

public record RefreshTokenRequestDTO(

        UUID refreshToken

) {
}
