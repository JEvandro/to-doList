package br.com.evandro.todoList.services.user;

import br.com.evandro.todoList.domains.resetpassword.ResetPasswordTokenEntity;
import br.com.evandro.todoList.domains.resetpassword.exceptions.RecuperationEmailNotFound;
import br.com.evandro.todoList.domains.resetpassword.exceptions.TokenInvalidException;
import br.com.evandro.todoList.domains.user.UserEntity;
import br.com.evandro.todoList.domains.user.exceptionsUser.MyAuthenticationException;
import br.com.evandro.todoList.domains.user.exceptionsUser.UpdatePasswordException;
import br.com.evandro.todoList.domains.user.exceptionsUser.UserNotFoundException;
import br.com.evandro.todoList.dto.user.AuthUserRequestDTO;
import br.com.evandro.todoList.dto.user.AuthUserResponseDTO;
import br.com.evandro.todoList.dto.user.ForgotPasswordRequestDTO;
import br.com.evandro.todoList.dto.user.ResetPasswordRequestDTO;
import br.com.evandro.todoList.providers.JWTProviderRefreshToken;
import br.com.evandro.todoList.providers.JWTProviderToken;
import br.com.evandro.todoList.repositories.ResetPasswordTokenRepository;
import br.com.evandro.todoList.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthUserService {

    @Value("${security.token.secret.user}")
    private String secret;

    @Value("${reset-password.token}")
    private Long resetPasswordTokenExpire;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTProviderToken jwtProviderToken;

    @Autowired
    private JWTProviderRefreshToken jwtProviderRefreshToken;

    @Autowired
    private ResetPasswordTokenRepository resetPasswordTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private LoginUserAttemptService loginUserAttemptService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthUserResponseDTO executeAuthUserSignin(AuthUserRequestDTO authUserRequestDTO, int attempt){
        var user = userRepository.findByUsernameIgnoringCase(authUserRequestDTO.username())
                .orElseThrow( () -> new UserNotFoundException("Username e/ou password estão incorretos"));

        loginUserAttemptService.isBlocked(user);

        if(!passwordEncoder.matches(authUserRequestDTO.password(), user.getPassword())) {
            loginUserAttemptService.failedLogin(attempt, user);
            throw new MyAuthenticationException("Username e/ou password estão incorretos");
        }

        var token = jwtProviderToken.generateToken(user);
        var refreshToken = jwtProviderRefreshToken.generateRefreshToken(user.getId());

        return new AuthUserResponseDTO(token, refreshToken.getId());
    }

    public void executeAuthUserForgotPassword(ForgotPasswordRequestDTO forgotPasswordRequestDTO) {
        var user = userRepository.findByEmailIgnoringCase(forgotPasswordRequestDTO.email()).orElseThrow(() ->
                new RecuperationEmailNotFound("Email not found")
        );

        var resetPassword = resetPasswordTokenRepository.save(new ResetPasswordTokenEntity(
                UUID.randomUUID().toString(),
                forgotPasswordRequestDTO.email(),
                Instant.now().plusMillis(resetPasswordTokenExpire).toEpochMilli())
        );

        String resetUrl = "http://localhost:8080/api/auth/reset-password";
        emailService.sendSimpleMessage(
                resetPassword.getEmail(),
                "Password Reset",
                "To reset your password, click the link below:\n" + resetUrl
                );
    }

    public void executeAuthUserResetPassword(ResetPasswordRequestDTO resetPasswordRequestDTO) {
        var resetPasswordToken = resetPasswordTokenRepository.
                findByToken(resetPasswordRequestDTO.token().toString()).orElseThrow(() ->
                        new TokenInvalidException("Token Inválido!")
                );

        if (Instant.ofEpochMilli(resetPasswordToken.getExpiresAt()).isBefore(Instant.now()) ){
            resetPasswordTokenRepository.deleteById(resetPasswordToken.getId());
            throw new TokenInvalidException("Token está expirado! Envie novamente um email");
        }

        var user = userRepository.findByEmailIgnoringCase(resetPasswordToken.getEmail())
                .orElseThrow(() -> new RecuperationEmailNotFound("Email not found"));

        if(passwordEncoder.matches(resetPasswordRequestDTO.password(), user.getPassword()))
            throw new UpdatePasswordException("A nova senha deve ser diferente da antiga!");

        if(!resetPasswordRequestDTO.password().equals(resetPasswordRequestDTO.confirmPassword()))
            throw new UpdatePasswordException("A nova senha e a senha de confirmação devem ser iguais.");

        user.setPassword(passwordEncoder.encode(resetPasswordRequestDTO.password()));
        userRepository.save(user);

        resetPasswordTokenRepository.deleteById(resetPasswordToken.getId());
    }

}
