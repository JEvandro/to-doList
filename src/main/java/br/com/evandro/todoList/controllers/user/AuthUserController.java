package br.com.evandro.todoList.controllers.user;

import br.com.evandro.todoList.domains.user.UserEntity;
import br.com.evandro.todoList.dto.user.AuthUserRequestDTO;
import br.com.evandro.todoList.services.user.AuthUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @Tag(name = "Autenticação", description = "Autenticação do usuário")
    @Operation(summary = "Autenticação do usuário", description = "Rota responsável por receber o login e senha do usuário e autenticar")
    public ResponseEntity authUser(@RequestBody AuthUserRequestDTO authUserRequestDTO){
        var result = authUserService.executeAuthUser(authUserRequestDTO);
        return ResponseEntity.ok().body(result);
    }

}
