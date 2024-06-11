package br.com.evandro.todoList.services.user;

import br.com.evandro.todoList.domains.codeconfirmation.CodeConfirmationEntity;
import br.com.evandro.todoList.domains.resetpassword.exceptions.TokenInvalidException;
import br.com.evandro.todoList.domains.user.UserEntity;
import br.com.evandro.todoList.repositories.CodeConfirmationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
public class CodeConfirmationService {

    private static final int CODE_LENGTH = 6;
    private static final Random RANDOM = new Random();
    private static final Set<String> usedCodes = new HashSet<>();

    @Autowired
    private CodeConfirmationRepository codeConfirmationRepository;

    @Value("${user-confirmation.code.expiry}")
    private Long codeExpires;

    public String generateRecoveryCode(UserEntity user) {
        String code;
        do {
            code = generateRandomCode();
        } while (usedCodes.contains(code));
        usedCodes.add(code);
        var expiresAt = Instant.now().plusMillis(codeExpires).toEpochMilli();
        codeConfirmationRepository.save(new CodeConfirmationEntity(code, user.getId(), expiresAt));
        return code;
    }

    private String generateRandomCode() {
        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int digit = RANDOM.nextInt(10);
            codeBuilder.append(digit);
        }
        return codeBuilder.toString();
    }

    public void isExpires(CodeConfirmationEntity code){
        if(Instant.now().isAfter(Instant.ofEpochMilli(code.getCodeExpiresAt()))){
            codeConfirmationRepository.delete(code);
            throw new TokenInvalidException("code expires! A new email will sent");
        }
    }


}
