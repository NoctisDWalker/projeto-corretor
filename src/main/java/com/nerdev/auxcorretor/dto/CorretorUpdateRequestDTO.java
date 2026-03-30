package com.nerdev.auxcorretor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema
public record CorretorUpdateRequestDTO(

        @NotBlank(message = "Campo obrigatorio")
        String nome,

        @NotBlank(message = "Campo obrigatorio")
        @Email
        String email,

        @NotBlank(message = "Campo obrigatorio")
        String telefone,

        @NotBlank(message = "Campo obrigatorio")
        String creci

) {
}
