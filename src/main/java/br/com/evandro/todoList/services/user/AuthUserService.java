package br.com.evandro.todoList.services.user;

import br.com.evandro.todoList.domains.refreshtoken.RefreshTokenEntity;
import br.com.evandro.todoList.domains.user.exceptionsUser.MyAuthenticationException;
import br.com.evandro.todoList.domains.user.exceptionsUser.UserNotFoundException;
import br.com.evandro.todoList.dto.user.AuthUserRequestDTO;
import br.com.evandro.todoList.dto.user.AuthUserResponseDTO;
import br.com.evandro.todoList.providers.JWTProviderRefreshToken;
import br.com.evandro.todoList.providers.JWTProviderToken;
import br.com.evandro.todoList.repositories.RefreshTokenRepository;
import br.com.evandro.todoList.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {

    @Value("${security.token.secret.user}")
    private String secret;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    JWTProviderToken jwtProviderToken;

    @Autowired
    JWTProviderRefreshToken jwtProviderRefreshToken;

    @Autowired
    PasswordEncoder passwordEncoder;

    public AuthUserResponseDTO executeAuthUser(AuthUserRequestDTO authUserRequestDTO){
        var user = userRepository.findByUsernameIgnoringCase(authUserRequestDTO.username())
                .orElseThrow( () -> new UserNotFoundException("Username e/ou password estão incorretos"));

        if(!passwordEncoder.matches(authUserRequestDTO.password(), user.getPassword()))
            throw new MyAuthenticationException("Username e/ou password estão incorretos");

        var token = jwtProviderToken.generateToken(user);
        var refreshToken = jwtProviderRefreshToken.generateRefreshToken(user.getId());

        return new AuthUserResponseDTO(token, refreshToken.getId());
    }

}
