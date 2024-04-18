package br.com.evandro.todoList.controllers.task;

import br.com.evandro.todoList.dto.task.TaskRequestDTO;
import br.com.evandro.todoList.services.task.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping("")
    public ResponseEntity create(@Valid @RequestBody TaskRequestDTO requestDTO){
        var result = taskService.executeCreate(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity update(@PathVariable UUID id, @RequestBody String description){
        var result = taskService.executeUpdate(description, id);
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/update/{id}/completed")
    public ResponseEntity updateCompleted(@PathVariable UUID id){
        var result = taskService.executeUpdateCompleted(id);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id){
        taskService.executeDelete(id);
        return ResponseEntity.noContent().build();
    }
}
