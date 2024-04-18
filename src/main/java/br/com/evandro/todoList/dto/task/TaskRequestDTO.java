package br.com.evandro.todoList.dto.task;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record TaskRequestDTO(@NotBlank String description, UUID userId) {
}
