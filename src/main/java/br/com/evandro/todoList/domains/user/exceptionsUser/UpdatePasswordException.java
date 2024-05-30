package br.com.evandro.todoList.domains.user.exceptionsUser;

public class UpdatePasswordException extends RuntimeException{

    public UpdatePasswordException(String message){
        super(message);
    }

}
