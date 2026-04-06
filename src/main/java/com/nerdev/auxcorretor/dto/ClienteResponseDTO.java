package com.nerdev.auxcorretor.dto;

import com.nerdev.auxcorretor.model.enums.StatusClienteEnum;

import java.util.UUID;

public record ClienteResponseDTO(
        UUID id,
        String nome,
        String telefone,
        String email,
        String observacoes,
        StatusClienteEnum statusCliente,
        UUID idCorretor
) {
}
