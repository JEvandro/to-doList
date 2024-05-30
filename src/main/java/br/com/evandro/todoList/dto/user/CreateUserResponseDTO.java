package br.com.evandro.todoList.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public record CreateUserResponseDTO(


        @Schema(example = "admin")
        String name,

        @Schema(example = "admin")
        String username,

        @Schema(example = "admin@gmail.com")
        String email,

        //@Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
        String access_token,

        //@Schema(example = "17452379930")
        Date expireAt

){
}
