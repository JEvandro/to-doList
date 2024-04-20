package br.com.evandro.todoList.controllers.user;

import br.com.evandro.todoList.domains.user.UserEntity;
import br.com.evandro.todoList.dto.user.AuthUserRequestDTO;
import br.com.evandro.todoList.services.user.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class AuthUserController {

    @Autowired
    AuthUserService authUserService;

    @PostMapping("/auth")
    public ResponseEntity authUser(@RequestBody AuthUserRequestDTO authUserRequestDTO){
        var result = authUserService.executeAuthUser(authUserRequestDTO);
        return ResponseEntity.ok().body(result);
    }

}
