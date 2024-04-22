package br.com.evandro.todoList.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponseDTO(

        @Schema(example = "Jose Evandro")
        String name,

        @Schema(example = "joseevandro")
        String username,

        @Schema(example = "jose@gmail.com")
        String email,

        @Schema(example = "$2a$10$/1HQFwkluYFXzARyhvW4ueh/vGEYj.uUOt7cTRI9weqrjb/tQEO6q")
        String password

){
}
