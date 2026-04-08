package com.nerdev.auxcorretor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record ClienteCreateRequestDTO(
        @NotBlank(message = "Campo obrigatorio")
        String nome,
        @CPF
        String cpf,
        @NotBlank(message = "Campo obrigatorio")
        String telefone,
        @Email
        String email,
        String observacoes
) {

}
