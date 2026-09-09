package com.nerdev.auxcorretor.dto.cliente;

import com.nerdev.auxcorretor.model.enums.StatusClienteEnum;

import java.util.UUID;

public record ClienteResumoDTO(
        UUID id,
        String nome,
        String cpf,
        StatusClienteEnum statusCliente
) {
}
