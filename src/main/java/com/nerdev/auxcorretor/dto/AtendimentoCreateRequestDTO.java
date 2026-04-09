package com.nerdev.auxcorretor.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AtendimentoCreateRequestDTO(
        @NotNull(message = "Campo obrigatorio")
        UUID idCliente,
        @NotNull(message = "Campo obrigatorio")
        UUID idCorretor,
        String observacoes
) {
}
