package com.nerdev.auxcorretor.dto;

import com.nerdev.auxcorretor.model.enums.StatusVisitaEnum;
import com.nerdev.auxcorretor.model.enums.TipoHistoricoVisitaEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public record HistoricoVisitaResponseDTO(
        UUID id,
        String descricao,
        StatusVisitaEnum statusVisitaAtual,
        TipoHistoricoVisitaEnum  tipoHistorico,
        LocalDateTime dataHoraCadastro
) {
}
