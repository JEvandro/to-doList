package br.com.evandro.todoList.domains.resetpassword.exceptions;

public class TokenInvalidException extends RuntimeException{

    public TokenInvalidException(String message){
        super(message);
    }

}
