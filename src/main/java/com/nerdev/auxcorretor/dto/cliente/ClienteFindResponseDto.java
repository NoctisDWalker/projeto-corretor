package com.nerdev.auxcorretor.dto.cliente;

import com.nerdev.auxcorretor.model.enums.StatusClienteEnum;

public record ClienteFindResponseDto(
        String nome,
        String cpf,
        String telefone,
        String email,
        String observacoes,
        StatusClienteEnum status
) {

}
