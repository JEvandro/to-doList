package br.com.evandro.todoList.services.user;

import br.com.evandro.todoList.domains.user.UserEntity;
import br.com.evandro.todoList.domains.user.exceptionsUser.MyAuthenticationException;
import br.com.evandro.todoList.domains.user.exceptionsUser.UserNotFoundException;
import br.com.evandro.todoList.dto.user.AuthUserDTO;
import br.com.evandro.todoList.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;

@Service
public class AuthUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserEntity executeAuthUser(AuthUserDTO authUserDTO){
        var user = userRepository.findByUsernameIgnoringCase(authUserDTO.username())
                .orElseThrow( () -> new UserNotFoundException("Username e/ou password estão incorretos"));

        if(!passwordEncoder.matches(authUserDTO.password(), user.getPassword()))
            throw new MyAuthenticationException("Username e/ou password estão incorretos");

        return user;
    }

}
