package br.com.evandro.todoList.domains.task.exceptionsTask;

public class TaskAccessNotPermittedException extends RuntimeException{

    public TaskAccessNotPermittedException(String message){
        super(message);
    }

}
