package br.com.evandro.todoList.domains.task.exceptionsTask;

public class TaskNotFoundException extends RuntimeException{

    public TaskNotFoundException(String message){
        super(message);
    }

}
