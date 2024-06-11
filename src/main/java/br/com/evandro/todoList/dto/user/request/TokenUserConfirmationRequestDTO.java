package br.com.evandro.todoList.dto.user.request;

public record TokenUserConfirmationRequestDTO(

        String token,
        String email

) {
}
