package br.com.evandro.todoList.services.user;

import br.com.evandro.todoList.domains.user.UserEntity;
import br.com.evandro.todoList.domains.user.UserStatusEnum;
import br.com.evandro.todoList.domains.user.exceptionsUser.UserBlockedException;
import br.com.evandro.todoList.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class LoginUserAttemptService {

    @Autowired
    private UserRepository userRepository;

    private static final int MAX_ATTEMPT = 4;

    public void failedLogin(int attempt, UserEntity user){
        if(attempt==MAX_ATTEMPT){
            attempt = 0;
            user.setUserStatus(UserStatusEnum.BLOCKED);
            user.setExpiresBlockedAt(Instant.now().plusMillis(600000).toEpochMilli());
            userRepository.save(user);
            throw new UserBlockedException("Sua conta está bloqueada temporariamente por 24h devido a várias tentativas de login");
        }
    }

    public void isBlocked(UserEntity user){
        if(user.getUserStatus().getStatus().equals("B")){
            if(Instant.now().isBefore(Instant.ofEpochMilli(user.getExpiresBlockedAt()))) {
                throw new UserBlockedException("Não foi possível realizar o login devido ao bloqueio temporário de sua conta, tente novamente mais tarde");
            }else {
                user.setUserStatus(UserStatusEnum.ACTIVE);
                user.setExpiresBlockedAt(null);
                userRepository.save(user);
            }
        }
    }

}
