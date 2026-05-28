package com.nerdev.auxcorretor.dto.corretor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.br.CPF;

@Schema
public record CorretorUpdateRequestDTO(

        String nome,
        String bio,
        @CPF
        String cpf,
        @Email
        String email,
        String telefone,
        String creci

) {
}
