package br.com.evandro.todoList.dto.user.response;

import java.time.LocalDateTime;

public record UpdateProfileUserResponseDTO(

        String name,
        String username,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updateAt

) {
}
