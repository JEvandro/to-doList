package br.com.evandro.todoList.domains.user.exceptionsUser;

public class UserBlockedException extends RuntimeException{

    public UserBlockedException(String message){
        super(message);
    }

}
