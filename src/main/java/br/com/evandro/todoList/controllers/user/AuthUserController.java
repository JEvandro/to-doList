package br.com.evandro.todoList.controllers.user;

import br.com.evandro.todoList.domains.user.UserEntity;
import br.com.evandro.todoList.dto.exceptions.ErrorResponseDTO;
import br.com.evandro.todoList.dto.task.UpdateTaskResponseDTO;
import br.com.evandro.todoList.dto.user.AuthUserRequestDTO;
import br.com.evandro.todoList.dto.user.AuthUserResponseDTO;
import br.com.evandro.todoList.services.user.AuthUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class AuthUserController {

    @Autowired
    AuthUserService authUserService;

    @PostMapping("/auth")
    @Tag(name = "Autenticação", description = "Autenticação do usuário")
    @Operation(summary = "Autenticação do usuário", description = "Rota responsável por receber o login e senha do usuário e autenticar")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            schema = @Schema(implementation = AuthUserResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "401",content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            })
    })
    public ResponseEntity authUser(@Valid @RequestBody AuthUserRequestDTO authUserRequestDTO){
        var result = authUserService.executeAuthUser(authUserRequestDTO);
        return ResponseEntity.ok().body(result);
    }

}
