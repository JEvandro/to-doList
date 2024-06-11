package br.com.evandro.todoList.dto.user.request;

import java.util.UUID;

public record ResetPasswordRequestDTO(

        UUID token,
        String password,
        String confirmPassword

) {
}
