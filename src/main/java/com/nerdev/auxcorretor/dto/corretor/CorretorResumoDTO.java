package com.nerdev.auxcorretor.dto.corretor;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema
public record CorretorResumoDTO(
        UUID id,
        String nome,
        String creci,
        Boolean ativo
) {
}
