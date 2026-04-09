package com.nerdev.auxcorretor.dto;

import com.nerdev.auxcorretor.model.enums.StatusAtendimentoEnum;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AtendimentoUpdateRequestDTO(
        @NotNull(message = "Status é obrigatório")
        StatusAtendimentoEnum status,
        String observacoes,
        LocalDateTime dataFim

) {
}
