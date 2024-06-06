package br.com.evandro.todoList.controllers.user;

import br.com.evandro.todoList.dto.exceptions.ErrorResponseDTO;
import br.com.evandro.todoList.dto.exceptions.HandlerExceptionMethodNotValidDTO;
import br.com.evandro.todoList.dto.task.AllTasksResponseDTO;
import br.com.evandro.todoList.dto.user.*;
import br.com.evandro.todoList.services.user.UserService;
import br.com.evandro.todoList.services.user.UsernameGeneratorUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuário", description = "Informações do Usuário e suas tasks")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UsernameGeneratorUserService generatorUsernameService;

    @PostMapping("")
    @Operation(summary = "Cadastro do usuário", description = "Rota responsável por criar o cadastro de um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(
                            schema = @Schema(implementation = CreateUserResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "409", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "406", content = {
                    @Content(
                            array = @ArraySchema(schema = @Schema(implementation = HandlerExceptionMethodNotValidDTO.class))
                    )
            }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            })
    })
    public ResponseEntity create(@Valid @RequestBody CreateUserRequestDTO request){
        var result = userService.executeCreate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/generate-username")
    public ResponseEntity generateUsername(){
        var result = generatorUsernameService.generateUniqueUsername();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "jwt_auth")
    @Operation(summary = "Perfil do usuário", description = "Rota responsável por mostrar as informações do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {

                    @Content(
                            schema = @Schema(implementation = GetUserResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            })
    })
    public ResponseEntity get(@PathVariable String username, HttpServletRequest request){
        var userId = request.getAttribute("userId");
        var result = userService.executeGet(username, UUID.fromString(userId.toString()));
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "jwt_auth")
    @Operation(summary = "Remoção do usuário", description = "Rota responsável por excluir o usuário e suas informações juntamente com as tasks criadas por ele")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            })
    })
    public ResponseEntity delete(HttpServletRequest request){
        var userId = request.getAttribute("userId");
        userService.executeDelete(UUID.fromString(userId.toString()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mytasks")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "jwt_auth")
    @Operation(summary = "Listagem de tarefas do usuário", description = "Rota responsável por mostrar todas as tarefas criadas pelo usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            array = @ArraySchema(schema = @Schema(implementation = AllTasksResponseDTO.class))
                    )
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            })
    })
    public ResponseEntity getAllTasks(HttpServletRequest request) {
        var userId = request.getAttribute("userId");
        var result = userService.executeGetAllTasks(UUID.fromString(userId.toString()));
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/password")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity updatePassword(
            @Valid @RequestBody UpdatePasswordRequestDTO updatePasswordRequestDTO,
            HttpServletRequest request
    ){
        var userId = request.getAttribute("userId");
        userService.executeUpdatePassword(updatePasswordRequestDTO, UUID.fromString(userId.toString()));
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update-profile")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity updateProfile(
            @Valid @RequestBody UpdateProfileUserRequestDTO updateProfileUserRequestDTO,
            HttpServletRequest request
    ){
        var userId = request.getAttribute("userId");
        var result = userService.executeUpdateProfile(updateProfileUserRequestDTO, UUID.fromString(userId.toString()));
        return ResponseEntity.ok().body(result);
    }


}
