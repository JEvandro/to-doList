package br.com.evandro.todoList.dto.user;

public record AuthUserResponseDTO(String access_token, Long expireAt) {
}
