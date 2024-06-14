package br.com.evandro.todoList.services.user;

import br.com.evandro.todoList.domains.user.UserEntity;
import br.com.evandro.todoList.domains.user.UserStatusEnum;
import br.com.evandro.todoList.domains.user.exceptionsUser.UserBlockedException;
import br.com.evandro.todoList.domains.userattempts.UserAttemptsEntity;
import br.com.evandro.todoList.repositories.ConfirmationCodeRepository;
import br.com.evandro.todoList.repositories.UserAttemptsRepository;
import br.com.evandro.todoList.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class LoginUserAttemptService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAttemptsRepository userAttemptsRepository;

    @Value("${user-blocked.expiry}")
    private Long blockedExpiresAt;

    private static final int MAX_ATTEMPT = 4;

    public void failedLogin(UserEntity user, UserAttemptsEntity userAttempts){
        if(userAttempts.getCountAttempts()==MAX_ATTEMPT){
            userAttempts.setCountAttempts(0);
            userAttempts.setPreviousStatus(user.getUserStatus());
            user.setUserStatus(UserStatusEnum.BLOCKED);
            user.setBlockedExpiresAt(Instant.now().plusMillis(blockedExpiresAt).toEpochMilli());
            userRepository.save(user);
            throw new UserBlockedException("Sua conta está bloqueada temporariamente por 24h devido a várias tentativas de login");
        }else{
            userAttempts.addAttempts();
        }
        userAttemptsRepository.save(userAttempts);
    }

    public void isBlocked(UserEntity user, UserAttemptsEntity userAttempts){
        if(user.getUserStatus().equals("B")){
            if(Instant.now().isBefore(Instant.ofEpochMilli(user.getBlockedExpiresAt()))) {
                throw new UserBlockedException("Não foi possível realizar o login devido ao bloqueio temporário de sua conta, tente novamente mais tarde");
            }else {
                if(userAttempts.getPreviousStatus().equals("P")) user.setUserStatus(UserStatusEnum.PENDENT);
                else user.setUserStatus(UserStatusEnum.ACTIVE);

                user.setBlockedExpiresAt(null);
                userRepository.save(user);
            }
        }
    }

}
