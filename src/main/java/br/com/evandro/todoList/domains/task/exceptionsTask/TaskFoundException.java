package br.com.evandro.todoList.domains.task.exceptionsTask;

public class TaskFoundException extends RuntimeException{

    public TaskFoundException(String message){
        super(message);
    }

}
