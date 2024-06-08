package br.com.evandro.todoList.domains.resetpassword.exceptions;

public class RecuperationEmailNotFound extends RuntimeException {

    public RecuperationEmailNotFound(String message){
        super(message);
    }

}
