package br.com.evandro.todoList.services.user;

import br.com.evandro.todoList.domains.resetpassword.ResetPasswordTokenEntity;
import br.com.evandro.todoList.domains.resetpassword.exceptions.RecuperationEmailNotFound;
import br.com.evandro.todoList.domains.resetpassword.exceptions.TokenInvalidException;
import br.com.evandro.todoList.domains.user.UserStatusEnum;
import br.com.evandro.todoList.domains.user.exceptionsUser.MyAuthenticationException;
import br.com.evandro.todoList.domains.user.exceptionsUser.UpdatePasswordException;
import br.com.evandro.todoList.domains.user.exceptionsUser.UserNotFoundException;
import br.com.evandro.todoList.dto.user.request.AuthUserRequestDTO;
import br.com.evandro.todoList.dto.user.request.ForgotPasswordRequestDTO;
import br.com.evandro.todoList.dto.user.request.ResetPasswordRequestDTO;
import br.com.evandro.todoList.dto.user.request.TokenUserConfirmationRequestDTO;
import br.com.evandro.todoList.dto.user.response.AuthUserResponseDTO;
import br.com.evandro.todoList.dto.user.response.UserConfirmationResponseDTO;
import br.com.evandro.todoList.providers.JWTProviderRefreshToken;
import br.com.evandro.todoList.providers.JWTProviderToken;
import br.com.evandro.todoList.repositories.CodeConfirmationRepository;
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

    @Value("${reset-password.token.expiry}")
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
    private LoginUserAttemptService loginUserAttemptService;

    @Autowired
    private CodeConfirmationRepository codeConfirmationRepository;

    @Autowired
    private CodeConfirmationService codeConfirmationService;

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

    public ResetPasswordTokenEntity executeAuthUserForgotPassword(ForgotPasswordRequestDTO forgotPasswordRequestDTO) {
        var user = userRepository.findByEmailIgnoringCase(forgotPasswordRequestDTO.email()).orElseThrow(() ->
                new RecuperationEmailNotFound("Email not found")
        );

        return resetPasswordTokenRepository.save(new ResetPasswordTokenEntity(
                UUID.randomUUID().toString(),
                forgotPasswordRequestDTO.email(),
                Instant.now().plusMillis(resetPasswordTokenExpire).toEpochMilli())
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

    public UserConfirmationResponseDTO executeAuthUserConfirmation(TokenUserConfirmationRequestDTO tokenUserConfirmationRequestDTO, UUID userId) {
        var code = codeConfirmationRepository.findByCodeAndUserId(tokenUserConfirmationRequestDTO.token(), userId).orElseThrow( () ->
                new TokenInvalidException("Code Invalid!")
        );

        var user = code.getUser();

        codeConfirmationService.isExpires(code);

        user.setUserStatus(UserStatusEnum.ACTIVE);
        codeConfirmationRepository.delete(code);

        return new UserConfirmationResponseDTO(user.getUsername(), UserStatusEnum.ACTIVE.getDescription());
    }
}
