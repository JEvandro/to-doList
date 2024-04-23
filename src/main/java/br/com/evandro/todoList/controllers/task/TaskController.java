package br.com.evandro.todoList.controllers.task;

import br.com.evandro.todoList.dto.exceptions.ErrorResponseDTO;
import br.com.evandro.todoList.dto.exceptions.HandlerExceptionMethodNotValidDTO;
import br.com.evandro.todoList.dto.task.*;
import br.com.evandro.todoList.services.task.TaskService;
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
@RequestMapping("/tasks")
@SecurityRequirement(name = "jwt_auth")
@Tag(name = "Tarefas", description = "Criação, deleção e atualização de tarefas")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping("")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Cadastro de tarefa", description = "Rota responsável por cadastrar a tarefa do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(
                            schema = @Schema(implementation = TaskResponseDTO.class)
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
            })
    })
    public ResponseEntity create(@Valid @RequestBody TaskRequestDTO requestDTO, HttpServletRequest request){
        var userId = request.getAttribute("userId");
        var result = taskService.executeCreate(requestDTO, UUID.fromString(userId.toString()));
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Atualização de tarefa", description = "Rota responsável por atualizar a decrição da tarefa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            schema = @Schema(implementation = UpdateTaskResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "406", content = {
                    @Content(
                            array = @ArraySchema(schema = @Schema(implementation = HandlerExceptionMethodNotValidDTO.class))
                    )
            })
    })
    public ResponseEntity update(@PathVariable UUID id, @Valid @RequestBody UpdateTaskRequestDTO request){
        var result = taskService.executeUpdate(request.description(), id);
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/update/{id}/completed")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Atualização de tarefa completada", description = "Rota responsável por atualizar a informação de se a tarefa foi completada")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            schema = @Schema(implementation = CompletedTaskResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            })
    })
    public ResponseEntity updateCompleted(@PathVariable UUID id){
        var result = taskService.executeUpdateCompleted(id);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Remoção de tarefa", description = "Rota responsável por deletar a tarefa")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            })
    })
    public ResponseEntity delete(@PathVariable UUID id){
        taskService.executeDelete(id);
        return ResponseEntity.noContent().build();
    }
}
