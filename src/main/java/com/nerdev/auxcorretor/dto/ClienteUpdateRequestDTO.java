package com.nerdev.auxcorretor.dto;

import com.nerdev.auxcorretor.model.enums.StatusClienteEnum;
import jakarta.validation.constraints.Email;

public record ClienteUpdateRequestDTO(
        String nome,
        String telefone,
        @Email
        String email,
        String observacoes,
        StatusClienteEnum statusCliente
) {

}
