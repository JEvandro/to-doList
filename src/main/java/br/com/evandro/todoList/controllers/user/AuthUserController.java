package br.com.evandro.todoList.controllers.user;


import br.com.evandro.todoList.dto.exceptions.ErrorResponseDTO;
import br.com.evandro.todoList.dto.exceptions.HandlerExceptionMethodNotValidDTO;
import br.com.evandro.todoList.dto.user.request.*;
import br.com.evandro.todoList.dto.user.response.AuthUserResponseDTO;
import br.com.evandro.todoList.dto.user.response.RefreshTokenResponseDTO;
import br.com.evandro.todoList.dto.user.response.UserConfirmationResponseDTO;
import br.com.evandro.todoList.providers.JWTProviderRefreshToken;
import br.com.evandro.todoList.services.user.AuthUserService;
import br.com.evandro.todoList.services.user.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Todo processo da etapa de autenticação do usuário")
public class AuthUserController {

    @Autowired
    AuthUserService authUserService;

    @Autowired
    JWTProviderRefreshToken jwtProviderRefreshToken;

    @Autowired
    EmailService emailService;

    @PostMapping("/sign-in")
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
    @Operation(summary = "Uso do refresh-token", description = "Rota responsável por receber o refresh-token e gerar um novo refresh-token e um novo token de autenticação")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            schema = @Schema(implementation = RefreshTokenResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "401", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            })
    })
    public ResponseEntity authRefreshUser(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        var result = jwtProviderRefreshToken.valideRefreshToken(refreshTokenRequestDTO.refreshToken());
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Esqueci a Senha", description = "Rota responsável por enviar ao seu e-mail um código de confirmação para redefinir a senha")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            })
    })
    public ResponseEntity authUserForgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO forgotPasswordRequestDTO){
        authUserService.executeAuthUserForgotPassword(forgotPasswordRequestDTO.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Redefinição de senha", description = "Rota responsável por receber o código validar e alterar a senha do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "498", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "406", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            })
    })
    public ResponseEntity authUserResetPassword(@Valid @RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO){
        authUserService.executeAuthUserResetPassword(resetPasswordRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/user-confirmation")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "jwt_auth")
    @Operation(summary = "Confirmação do usuário", description = "Rota responsável por realizar a confirmação do usuário via código por e-mail")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            schema = @Schema(implementation = UserConfirmationResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "498", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "406", content = {
                    @Content(
                            schema = @Schema(implementation = HandlerExceptionMethodNotValidDTO.class)
                    )
            })
    })
    public ResponseEntity authUserConfirmation(
            @Valid @RequestBody UserConfirmationCodeRequestDTO userConfirmationCodeRequestDTO,
            HttpServletRequest request
    ){
        var userId = request.getAttribute("userId").toString();
        var result = authUserService.executeAuthUserConfirmation(userConfirmationCodeRequestDTO.code(), UUID.fromString(userId));
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/send-email/user-confirmation")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "jwt_auth")
    @Operation(summary = "Email de confirmação de usuário", description = "Rota responsável por mandar o e-mail de confirmação do usuário, caso o código do primero e-mail enviado automaticamente já tenha expirado")
    @ApiResponse(responseCode = "200")
    public ResponseEntity authUserConfirmationEmail(HttpServletRequest request){
        var userId = request.getAttribute("userId").toString();
        emailService.sendMailToUserConfirmation(UUID.fromString(userId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/send-email/reset-password")
    @Operation(summary = "Email de reset de senha", description = "Rota responsável por mandar o e-mail de reset de senha, caso o código do primeiro e-mail enviado automaticamente já tenha expirado")
    @ApiResponse(responseCode = "200")
    public ResponseEntity authUserResetPasswordEmail(HttpServletRequest request){
        var userId = request.getAttribute("userId").toString();
        emailService.sendMailToResetPassword(UUID.fromString(userId));
        return ResponseEntity.ok().build();
    }

}
