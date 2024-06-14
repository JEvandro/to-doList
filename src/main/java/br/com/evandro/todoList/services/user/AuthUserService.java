package br.com.evandro.todoList.services.user;

import br.com.evandro.todoList.domains.resetpassword.exceptions.RecuperationEmailNotFound;
import br.com.evandro.todoList.domains.resetpassword.exceptions.CodeInvalidException;
import br.com.evandro.todoList.domains.user.UserStatusEnum;
import br.com.evandro.todoList.domains.user.exceptionsUser.MyAuthenticationException;
import br.com.evandro.todoList.domains.user.exceptionsUser.UpdatePasswordException;
import br.com.evandro.todoList.domains.user.exceptionsUser.UserNotFoundException;
import br.com.evandro.todoList.dto.user.request.AuthUserRequestDTO;
import br.com.evandro.todoList.dto.user.request.ResetPasswordRequestDTO;
import br.com.evandro.todoList.dto.user.request.UserConfirmationCodeRequestDTO;
import br.com.evandro.todoList.dto.user.response.AuthUserResponseDTO;
import br.com.evandro.todoList.dto.user.response.UserConfirmationResponseDTO;
import br.com.evandro.todoList.providers.JWTProviderRefreshToken;
import br.com.evandro.todoList.providers.JWTProviderToken;
import br.com.evandro.todoList.repositories.ConfirmationCodeRepository;
import br.com.evandro.todoList.repositories.ResetPasswordCodeRepository;
import br.com.evandro.todoList.repositories.UserAttemptsRepository;
import br.com.evandro.todoList.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthUserService {

    @Value("${security.token.secret.user}")
    private String secret;

    @Value("${reset-password.code.expiry}")
    private Long resetPasswordTokenExpire;

    @Autowired
    private UserAttemptsRepository userAttemptsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTProviderToken jwtProviderToken;

    @Autowired
    private JWTProviderRefreshToken jwtProviderRefreshToken;

    @Autowired
    private ResetPasswordCodeRepository resetPasswordCodeRepository;

    @Autowired
    private LoginUserAttemptService loginUserAttemptService;

    @Autowired
    private ConfirmationCodeRepository confirmationCodeRepository;

    @Autowired
    private CodeUserService codeUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public AuthUserResponseDTO executeAuthUserSignin(AuthUserRequestDTO authUserRequestDTO){
        var user = userRepository.findByUsernameIgnoringCase(authUserRequestDTO.username())
                .orElseThrow( () -> new UserNotFoundException("Username e/ou password estão incorretos"));

        var userAttempts = userAttemptsRepository.findByUserId(user.getId());

        loginUserAttemptService.isBlocked(user, userAttempts);

        if(!passwordEncoder.matches(authUserRequestDTO.password(), user.getPassword())) {
            loginUserAttemptService.failedLogin(user, userAttempts);
            throw new MyAuthenticationException("Username e/ou password estão incorretos");
        }

        var token = jwtProviderToken.generateToken(user);
        var refreshToken = jwtProviderRefreshToken.generateRefreshToken(user.getId());

        return new AuthUserResponseDTO(token, refreshToken.getId());
    }

    public void executeAuthUserForgotPassword(String userEmail) {
        var user = userRepository.findByEmailIgnoringCase(userEmail).orElseThrow(() ->
                new RecuperationEmailNotFound("Email not found")
        );

        emailService.sendMailToResetPassword(user.getId());
    }

    public void executeAuthUserResetPassword(ResetPasswordRequestDTO resetPasswordRequestDTO) {
        var resetPasswordCode = resetPasswordCodeRepository.
                findByCode(resetPasswordRequestDTO.code()).orElseThrow(() ->
                        new CodeInvalidException("Código Inválido!")
                );

        codeUserService.resetPasswordCodeIsExpires(resetPasswordCode);

        var user = userRepository.findByEmailIgnoringCase(resetPasswordCode.getEmail())
                .orElseThrow(() -> new RecuperationEmailNotFound("Email not found"));

        if(passwordEncoder.matches(resetPasswordRequestDTO.password(), user.getPassword()))
            throw new UpdatePasswordException("A nova senha deve ser diferente da antiga!");

        if(!resetPasswordRequestDTO.password().equals(resetPasswordRequestDTO.confirmPassword()))
            throw new UpdatePasswordException("A nova senha e a senha de confirmação devem ser iguais.");

        user.setPassword(passwordEncoder.encode(resetPasswordRequestDTO.password()));
        userRepository.save(user);

        resetPasswordCodeRepository.delete(resetPasswordCode);
    }

    public UserConfirmationResponseDTO executeAuthUserConfirmation(String requestCode, UUID userId) {
        var code = confirmationCodeRepository.findByCodeAndUserId(requestCode, userId).orElseThrow( () ->
                new CodeInvalidException("Code Invalid!")
        );

        codeUserService.confirmationCodeIsExpires(code);

        var user = code.getUser();
        user.setUserStatus(UserStatusEnum.ACTIVE);
        confirmationCodeRepository.delete(code);

        return new UserConfirmationResponseDTO(user.getUsername(), user.getUserStatusEnum().getDescription());
    }
}
