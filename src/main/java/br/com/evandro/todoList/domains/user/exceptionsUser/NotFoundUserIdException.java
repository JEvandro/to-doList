package br.com.evandro.todoList.domains.user.exceptionsUser;

public class NotFoundUserIdException extends RuntimeException {

    public NotFoundUserIdException(String message){
        super(message);
    }

}
