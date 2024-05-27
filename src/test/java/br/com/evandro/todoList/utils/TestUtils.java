package br.com.evandro.todoList.utils;

import br.com.evandro.todoList.domains.user.UserEntity;
import br.com.evandro.todoList.dto.user.AuthUserResponseDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

public class TestUtils {

    public static String ObjectToJSON(Object obj){
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateToken(UUID id, String secret){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        var expiresAt = Instant.now().plus(Duration.ofMinutes(30));

        var token = JWT.create()
                .withIssuer("TESTE")
                .withSubject(id.toString())
                .withClaim("roles", Arrays.asList("CANDIDATE"))
                .withExpiresAt(expiresAt)
                .sign(algorithm);

        return token;
    }

}
