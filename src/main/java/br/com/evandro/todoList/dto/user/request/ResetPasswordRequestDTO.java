package br.com.evandro.todoList.dto.user.request;

import java.util.UUID;

public record ResetPasswordRequestDTO(

        String code,
        String password,
        String confirmPassword

) {
}
