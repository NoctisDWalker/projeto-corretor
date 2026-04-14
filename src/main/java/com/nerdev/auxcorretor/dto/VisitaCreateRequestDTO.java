package com.nerdev.auxcorretor.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema
public record VisitaCreateRequestDTO(
        UUID atendimento,
        UUID imovel,
        LocalDateTime dataHoraAgenda,
        String observacoes
) {
}
