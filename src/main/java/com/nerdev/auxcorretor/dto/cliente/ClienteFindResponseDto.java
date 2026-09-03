package com.nerdev.auxcorretor.dto.cliente;

import com.nerdev.auxcorretor.model.enums.StatusClienteEnum;

import java.util.UUID;

public record ClienteFindResponseDto(
        UUID id,
        String nome,
        String cpf,
        String telefone,
        String email,
        String observacoes,
        StatusClienteEnum status
) {

}
