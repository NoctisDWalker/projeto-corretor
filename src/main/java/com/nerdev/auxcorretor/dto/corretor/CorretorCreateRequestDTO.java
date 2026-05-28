package com.nerdev.auxcorretor.dto.corretor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

@Schema
public record CorretorCreateRequestDTO(

        @NotBlank(message = "Campo obrigatorio")
        String nome,

        @NotBlank(message = "Campo obrigatorio")
        @CPF
        String cpf,

        String bio,

        @NotBlank(message = "Campo obrigatorio")
        @Email
        String email,

        String telefone,

        @NotBlank(message = "Campo obrigatorio")
        String creci

) {
}
