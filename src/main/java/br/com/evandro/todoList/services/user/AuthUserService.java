package br.com.evandro.todoList.services.user;

import br.com.evandro.todoList.domains.user.exceptionsUser.MyAuthenticationException;
import br.com.evandro.todoList.domains.user.exceptionsUser.UserNotFoundException;
import br.com.evandro.todoList.dto.user.AuthUserRequestDTO;
import br.com.evandro.todoList.dto.user.AuthUserResponseDTO;
import br.com.evandro.todoList.providers.JWTProvider;
import br.com.evandro.todoList.repositories.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Service
public class AuthUserService {

    @Value("${security.token.secret.user}")
    private String secret;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JWTProvider jwtProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    public AuthUserResponseDTO executeAuthUser(AuthUserRequestDTO authUserRequestDTO){
        var user = userRepository.findByUsernameIgnoringCase(authUserRequestDTO.username())
                .orElseThrow( () -> new UserNotFoundException("Username e/ou password estão incorretos"));

        if(!passwordEncoder.matches(authUserRequestDTO.password(), user.getPassword()))
            throw new MyAuthenticationException("Username e/ou password estão incorretos");

        var token = jwtProvider.generateToken(user);

        return new AuthUserResponseDTO(token, jwtProvider.extractExpiresAt(token));
    }

}
