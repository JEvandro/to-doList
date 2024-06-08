package br.com.evandro.todoList.providers;

import br.com.evandro.todoList.domains.refreshtoken.RefreshTokenEntity;
import br.com.evandro.todoList.domains.user.exceptionsUser.MyAuthenticationException;
import br.com.evandro.todoList.domains.user.exceptionsUser.UserNotFoundException;
import br.com.evandro.todoList.dto.user.RefreshTokenResponseDTO;
import br.com.evandro.todoList.repositories.RefreshTokenRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@Service
public class JWTProviderRefreshToken {

    @Value("${security.token.secret.user}")
    private String secret;

    @Value("${jwt.refresh.expiry}")
    private Long refreshTokenExpiry;

    @Value("${jwt.token.expiry}")
    private Long tokenExpiry;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenResponseDTO valideRefreshToken(UUID refreshToken){

        if(refreshToken.toString().isEmpty())
            throw new MyAuthenticationException("Token is invalid!");

        var refreshTokenEntity = refreshTokenRepository.findById(refreshToken).orElseThrow( () ->
                new MyAuthenticationException("Token is invalid!")
        );

        verifyExpirationTokenRefreshToken(refreshTokenEntity);

        Algorithm algorithm = Algorithm.HMAC256(secret);
        var expiresAt = Instant.now().plusMillis(tokenExpiry);

        var token = JWT.create()
                .withIssuer(refreshTokenEntity.getUser().getUsername())
                .withSubject(refreshTokenEntity.getUserId().toString())
                .withClaim("roles", Arrays.asList("USER"))
                .withExpiresAt(Date.from(expiresAt))
                .sign(algorithm);

        var newRefreshToken = generateRefreshToken(refreshTokenEntity.getUserId()).getId();
        refreshTokenRepository.delete(refreshTokenEntity);

        return new RefreshTokenResponseDTO(token, newRefreshToken);

    }

    public RefreshTokenEntity generateRefreshToken(UUID userId){
        var expiresAt = Instant.now().plusMillis(refreshTokenExpiry);
        return refreshTokenRepository.save(new RefreshTokenEntity(expiresAt.toEpochMilli(), userId));
    }

    public void verifyExpirationTokenRefreshToken(RefreshTokenEntity refreshToken) {
        if (Instant.ofEpochMilli(refreshToken.getExpiresIn()).isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new MyAuthenticationException("Refresh token was expired. Please make a new signin request");
        }
    }

    public void deleteByUserId(UUID userId){
        var refresh = refreshTokenRepository.findByUserId(userId).orElseThrow(() ->
            new UserNotFoundException("Não existe refresh token para o usuário cadastrado com este id: " + userId)
        );
        refreshTokenRepository.delete(refresh);
    }

}
