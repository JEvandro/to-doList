package br.com.evandro.todoList.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserConfirmationCodeRequestDTO(

        @Schema(example = "364762", requiredMode = Schema.RequiredMode.REQUIRED)
        @Length(min = 6, max = 6, message = "O campo [code] deve conter exatamente 6 carateres")
        @NotBlank
        String code

) {
}
