package br.com.evandro.todoList.controllers.user;


import br.com.evandro.todoList.dto.exceptions.ErrorResponseDTO;
import br.com.evandro.todoList.dto.user.*;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthUserController {

    @Autowired
    AuthUserService authUserService;

    @Autowired
    JWTProviderRefreshToken jwtProviderRefreshToken;

    @PostMapping("/sign-in")
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
    public ResponseEntity authUserSignin(@Valid @RequestBody AuthUserRequestDTO authUserRequestDTO){
        var result = authUserService.executeAuthUserSignin(authUserRequestDTO);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity authRefreshUser(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        var result = jwtProviderRefreshToken.valideRefreshToken(refreshTokenRequestDTO.refreshToken());
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity authUserForgotPassword(@RequestBody ForgotPasswordRequestDTO forgotPasswordRequestDTO){
        authUserService.executeAuthUserForgotPassword(forgotPasswordRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity authUserResetPassword(@RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO){
        authUserService.executeAuthUserResetPassword(resetPasswordRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
