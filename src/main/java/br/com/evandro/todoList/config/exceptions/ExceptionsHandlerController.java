package br.com.evandro.todoList.config.exceptions;

import br.com.evandro.todoList.domains.task.exceptionsTask.TaskAccessNotPermittedException;
import br.com.evandro.todoList.domains.task.exceptionsTask.TaskFoundException;
import br.com.evandro.todoList.domains.task.exceptionsTask.TaskNotFoundException;
import br.com.evandro.todoList.domains.user.exceptionsUser.MyAuthenticationException;
import br.com.evandro.todoList.domains.user.exceptionsUser.UserFoundException;
import br.com.evandro.todoList.domains.user.exceptionsUser.UserNotFoundException;
import br.com.evandro.todoList.dto.exceptions.ErrorResponseDTO;
import br.com.evandro.todoList.dto.exceptions.HandlerExceptionMethodNotValidDTO;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class ExceptionsHandlerController {

    private MessageSource messageSource;

    public ExceptionsHandlerController(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<HandlerExceptionMethodNotValidDTO>> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e){
        List<HandlerExceptionMethodNotValidDTO> listDTO = new ArrayList<>();

        e.getBindingResult().getFieldErrors().forEach( err -> {
            String message = messageSource.getMessage(err, LocaleContextHolder.getLocale());
            listDTO.add(new HandlerExceptionMethodNotValidDTO(message, err.getField()));
        });
        return new ResponseEntity<>(listDTO, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(UserFoundException.class)
    public ResponseEntity handlerUserFoundException(UserFoundException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handlerUserNotFoundException(UserNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity handlerTaskNotFoundException(TaskNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity handlerNoResourceFoundException(NoResourceFoundException e){
        return ResponseEntity.badRequest().body(new ErrorResponseDTO("Recurso a ser passado é obrigatório"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity handlerMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ErrorResponseDTO("UUID Invalido: " + Objects.requireNonNull(e.getValue()).toString()));
    }

    @ExceptionHandler(TaskFoundException.class)
    public ResponseEntity handlerTaskFoundException(TaskFoundException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handlerHttpMessageNotReadableException(HttpMessageNotReadableException e){
        return ResponseEntity.badRequest().body(
                new ErrorResponseDTO("O argumento deve ser passado obrigatoriamente"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handlerDataIntegrityViolationException(DataIntegrityViolationException e){
        return ResponseEntity.badRequest().body(new ErrorResponseDTO("Id do usuário é obrigatório"));
    }

    @ExceptionHandler(MyAuthenticationException.class)
    public ResponseEntity handlerMyAuthenticationException(MyAuthenticationException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(TaskAccessNotPermittedException.class)
    public ResponseEntity handlerTaskAccessNotPermitted(TaskAccessNotPermittedException e){
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(e.getMessage()));
    }

}
