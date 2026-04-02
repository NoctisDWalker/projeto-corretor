package com.nerdev.auxcorretor.dto;

import com.nerdev.auxcorretor.model.StatusClienteEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ClienteResquestDTO(
        @NotBlank
        String nome,
        @NotBlank
        String telefone,
        @NotBlank
        @Email
        String email,
        String observacoes,
        @NotNull
        StatusClienteEnum statusCliente,
        @NotNull
        UUID idCorretor
) {

}
