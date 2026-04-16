package com.nerdev.auxcorretor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema
public record VisitaCreateRequestDTO(
        @NotNull(message = "Campo obrigatorio.")
        UUID idImovel,
        @NotNull(message = "Campo obrigatorio.")
        LocalDateTime dataHoraAgendada,
        String observacoes
) {
}
