package br.com.evandro.todoList.dto.user;

import java.util.UUID;

public record ResetPasswordRequestDTO(

        UUID token,
        String password,
        String confirmPassword

) {
}
