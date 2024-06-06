package br.com.evandro.todoList.services.user;

import br.com.evandro.todoList.domains.user.UserEntity;
import br.com.evandro.todoList.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class UsernameGeneratorUserService {

    @Autowired
    private UserRepository userRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int USERNAME_LENGTH = 32;
    private final Random random = new SecureRandom();

    public String generateUniqueUsername() {
        String username;
        do {
            username = generateRandomUsername();
        } while (userRepository.existsByUsername(username));
        return username;
    }

    private String generateRandomUsername() {
        StringBuilder username = new StringBuilder(USERNAME_LENGTH);
        for (int i = 0; i < USERNAME_LENGTH; i++) {
            username.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return username.toString();
    }

}
