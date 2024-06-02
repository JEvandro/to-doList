package br.com.evandro.todoList.providers;

import br.com.evandro.todoList.domains.refreshtoken.RefreshTokenEntity;
import br.com.evandro.todoList.domains.user.exceptionsUser.MyAuthenticationException;
import br.com.evandro.todoList.dto.user.RefreshTokenResponseDTO;
import br.com.evandro.todoList.repositories.RefreshTokenRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Service
public class JWTProviderRefreshToken {

    @Value("${security.token.secret.user}")
    private String secret;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenResponseDTO valideRefreshToken(UUID refreshToken){

        if(refreshToken.toString().isEmpty())
            throw new MyAuthenticationException("Token is invalid!");

        var refreshTokenEntity = refreshTokenRepository.findById(refreshToken).orElseThrow( () ->
                new MyAuthenticationException("Token is invalid!")
        );

        if(Instant.now().isAfter(Instant.ofEpochSecond(refreshTokenEntity.getExpiresIn())))
            throw new MyAuthenticationException("Token is invalid!");

        Algorithm algorithm = Algorithm.HMAC256(secret);
        var expiresAt = Instant.now().plus(Duration.ofMinutes(2));

        var token = JWT.create()
                .withIssuer(refreshTokenEntity.getUser().getUsername())
                .withSubject(refreshTokenEntity.getUserId().toString())
                .withClaim("roles", Arrays.asList("USER"))
                .withExpiresAt(expiresAt)
                .sign(algorithm);

        return new RefreshTokenResponseDTO(token);

    }

    public RefreshTokenEntity generateRefreshToken(UUID userId){
        var expiresAt = Instant.now().plus(Duration.ofHours(1));
        return refreshTokenRepository.save(new RefreshTokenEntity(expiresAt.getEpochSecond(), userId));
    }

}
