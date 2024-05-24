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
@RequestMapping("/api/tasks")
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
            }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            })
    })
    public ResponseEntity create(@Valid @RequestBody TaskRequestDTO requestDTO, HttpServletRequest request){
        var userId = request.getAttribute("userId");
        var result = taskService.executeCreate(requestDTO, UUID.fromString(userId.toString()));
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("{taskId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Detalhes de uma tarefa", description = "Rota responsável por buscar todas as informações de uma tarefa específica do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            schema = @Schema(implementation = AllTasksResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            })
    })
    public ResponseEntity get(@PathVariable UUID taskId, HttpServletRequest request){
        var userId = request.getAttribute("userId");
        var result = taskService.executeGet(taskId, UUID.fromString(userId.toString()));
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/{taskId}")
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
            }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            })
    })
    public ResponseEntity update(@PathVariable UUID taskId, @Valid @RequestBody UpdateTaskRequestDTO updateRequest, HttpServletRequest request){
        var userId = request.getAttribute("userId");
        var result = taskService.executeUpdate(updateRequest.description(), taskId, UUID.fromString(userId.toString()));
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/{taskId}/complete")
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
            }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            })
    })
    public ResponseEntity updateCompleted(@PathVariable UUID taskId, HttpServletRequest request){
        var userId = request.getAttribute("userId");
        var result = taskService.executeUpdateCompleted(taskId, UUID.fromString(userId.toString()));
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Remoção de tarefa", description = "Rota responsável por deletar a tarefa")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            })
    })
    public ResponseEntity delete(@PathVariable UUID taskId, HttpServletRequest request){
        var userId = request.getAttribute("userId");
        taskService.executeDelete(taskId, UUID.fromString(userId.toString()));
        return ResponseEntity.noContent().build();
    }
}
