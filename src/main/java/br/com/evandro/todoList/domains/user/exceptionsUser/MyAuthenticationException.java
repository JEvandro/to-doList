package br.com.evandro.todoList.domains.user.exceptionsUser;

import org.springframework.security.core.AuthenticationException;

public class MyAuthenticationException extends RuntimeException {

    public MyAuthenticationException(String message){
        super(message);
    }

}
