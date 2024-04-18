package br.com.evandro.todoList.domains.user.exceptionsUser;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message){
        super(message);
    }

}
