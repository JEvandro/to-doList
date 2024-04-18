package br.com.evandro.todoList.controllers.user;

import br.com.evandro.todoList.dto.user.UserRequestDTO;
import br.com.evandro.todoList.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("")
    public ResponseEntity create(@Valid @RequestBody UserRequestDTO request){
        var result = userService.executeCreate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{username}")
    public ResponseEntity get(@PathVariable String username){
        var result = userService.executeGet(username);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity delete(@PathVariable UUID userId){
        userService.executeDelete(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/mytasks")
    public ResponseEntity getAllTasks(@PathVariable UUID userId){
        var result = userService.executeGetAllTasks(userId);
        return ResponseEntity.ok().body(result);
    }


}
