package br.com.evandro.todoList.controllers.user;


import br.com.evandro.todoList.dto.exceptions.ErrorResponseDTO;
import br.com.evandro.todoList.dto.user.request.*;
import br.com.evandro.todoList.dto.user.response.AuthUserResponseDTO;
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
public class AuthUserController {

    @Autowired
    AuthUserService authUserService;

    @Autowired
    JWTProviderRefreshToken jwtProviderRefreshToken;

    @Autowired
    EmailService emailService;

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
        authUserService.executeAuthUserForgotPassword(forgotPasswordRequestDTO.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity authUserResetPassword(@RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO){
        authUserService.executeAuthUserResetPassword(resetPasswordRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/user-confirmation")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity authUserConfirmation(
            @RequestBody UserConfirmationCodeRequestDTO userConfirmationCodeRequestDTO,
            HttpServletRequest request
    ){
        var userId = request.getAttribute("userId").toString();
        var result = authUserService.executeAuthUserConfirmation(userConfirmationCodeRequestDTO.code(), UUID.fromString(userId));
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/send-email/user-confirmation")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity authUserConfirmationEmail(HttpServletRequest request){
        var userId = request.getAttribute("userId").toString();
        emailService.sendMailToUserConfirmation(UUID.fromString(userId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/send-email/reset-password")
    public ResponseEntity authUserResetPasswordEmail(HttpServletRequest request){
        var userId = request.getAttribute("userId").toString();
        emailService.sendMailToResetPassword(UUID.fromString(userId));
        return ResponseEntity.ok().build();
    }

}
