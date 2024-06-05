package br.com.evandro.todoList.providers;

import br.com.evandro.todoList.domains.user.UserEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

@Service
public class JWTProviderToken {

    @Value("${security.token.secret.user}")
    private String secret;

    @Value("${jwt.token.expiry}")
    private Long tokenExpiry;

    public DecodedJWT valideToken(String token){
        token = token.replace("Bearer ", "");

        Algorithm algorithm = Algorithm.HMAC256(secret);

        try {
            var tokenDecoded = JWT.require(algorithm)
                    .build()
                    .verify(token);

            return tokenDecoded;
        } catch (JWTVerificationException e) {
            e.getStackTrace();
            return null;
        }
    }

    public String generateToken(UserEntity user){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        var expiresAt = Instant.now().plusMillis(tokenExpiry);

        return JWT.create()
                .withIssuer(user.getName())
                .withSubject(user.getId().toString())
                .withClaim("roles", Arrays.asList("USER"))
                .withExpiresAt(Date.from(expiresAt))
                .sign(algorithm);
    }

    public Date extractExpiresAt(String token){
        DecodedJWT decodedJWT = valideToken(token);
        if (decodedJWT != null) {
            return decodedJWT.getExpiresAt();
        }
        return null;
    }



}
