package br.com.evandro.todoList.services.user;

import br.com.evandro.todoList.domains.codeconfirmation.CodeConfirmationEntity;
import br.com.evandro.todoList.domains.resetpassword.ResetPasswordCodeEntity;
import br.com.evandro.todoList.domains.resetpassword.exceptions.CodeInvalidException;
import br.com.evandro.todoList.domains.user.UserEntity;
import br.com.evandro.todoList.repositories.ConfirmationCodeRepository;
import br.com.evandro.todoList.repositories.ResetPasswordCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
public class CodeUserService {

    private static final int CODE_LENGTH = 6;
    private static final Random RANDOM = new Random();
    private static final Set<String> usedCodes = new HashSet<>();

    @Autowired
    private ConfirmationCodeRepository confirmationCodeRepository;

    @Autowired
    private ResetPasswordCodeRepository resetPasswordCodeRepository;

    @Value("${user-confirmation.code.expiry}")
    private Long confirmationCodeExpires;

    @Value("${reset-password.code.expiry}")
    private Long resetPasswordCodeExpires;

    public String generateConfirmationCode(UserEntity user) {
        var code = generateRandomCode();
        var expiresAt = Instant.now().plusMillis(confirmationCodeExpires).toEpochMilli();
        confirmationCodeRepository.save(new CodeConfirmationEntity(code, user.getId(), expiresAt));
        return code;
    }

    public ResetPasswordCodeEntity generateResetPasswordCode(UserEntity user){
        var code = generateRandomCode();
        var expiresAt = Instant.now().plusMillis(resetPasswordCodeExpires).toEpochMilli();
        return resetPasswordCodeRepository.save(new ResetPasswordCodeEntity(code, user.getEmail(), expiresAt, user.getId()));
    }


    private static String generateRandomCode() {
        if(usedCodes.size() == Math.pow(10,CODE_LENGTH)){
            usedCodes.clear();
        }

        String code;
        do {
            StringBuilder codeBuilder = new StringBuilder();
            for (int i = 0; i < CODE_LENGTH; i++) {
                int digit = RANDOM.nextInt(10);
                codeBuilder.append(digit);
            }
            code = codeBuilder.toString();
        } while (usedCodes.contains(code));
        usedCodes.add(code);
        return code;
    }

    public void confirmationCodeIsExpires(CodeConfirmationEntity code){
        if(Instant.now().isAfter(Instant.ofEpochMilli(code.getExpiresAt()))){
            confirmationCodeRepository.delete(code);
            throw new CodeInvalidException("code expires! send a new email");
        }
    }

    public void resetPasswordCodeIsExpires(ResetPasswordCodeEntity code){
        if(Instant.now().isAfter(Instant.ofEpochMilli(code.getExpiresAt()))){
            resetPasswordCodeRepository.delete(code);
            throw new CodeInvalidException("code expires! send a new email");
        }
    }


}
