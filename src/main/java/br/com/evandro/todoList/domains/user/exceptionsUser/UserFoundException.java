package br.com.evandro.todoList.domains.user.exceptionsUser;

public class UserFoundException extends RuntimeException{

    public UserFoundException(String message){
        super(message);
    }

}
