package br.com.evandro.todoList.controllers.user;


import br.com.evandro.todoList.dto.exceptions.ErrorResponseDTO;
import br.com.evandro.todoList.dto.user.AuthUserRequestDTO;
import br.com.evandro.todoList.dto.user.AuthUserResponseDTO;
import br.com.evandro.todoList.dto.user.RefreshTokenRequestDTO;
import br.com.evandro.todoList.providers.JWTProviderRefreshToken;
import br.com.evandro.todoList.services.user.AuthUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthUserController {

    @Autowired
    AuthUserService authUserService;

    @Autowired
    JWTProviderRefreshToken jwtProviderRefreshToken;

    @PostMapping("/signin")
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

    @PostMapping("/refresh-token")
    public ResponseEntity authRefreshUser(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        var result = jwtProviderRefreshToken.valideRefreshToken(refreshTokenRequestDTO.refreshToken());
        return ResponseEntity.ok().body(result);
    }

}
