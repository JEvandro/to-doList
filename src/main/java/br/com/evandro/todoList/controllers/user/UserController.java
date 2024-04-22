package br.com.evandro.todoList.controllers.user;

import br.com.evandro.todoList.dto.user.UserRequestDTO;
import br.com.evandro.todoList.services.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity get(@PathVariable String username){
        var result = userService.executeGet(username);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity delete(HttpServletRequest request){
        var userId = request.getAttribute("userId");
        userService.executeDelete(UUID.fromString(userId.toString()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mytasks")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity getAllTasks(HttpServletRequest request){
        var userId = request.getAttribute("userId");
        var result = userService.executeGetAllTasks(UUID.fromString(userId.toString()));
        return ResponseEntity.ok().body(result);
    }


}
