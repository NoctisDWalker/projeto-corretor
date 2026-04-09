package com.nerdev.auxcorretor.dto;

import com.nerdev.auxcorretor.model.enums.StatusAtendimentoEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema
public record AtendimentoResponseDTO(
        UUID id,
        UUID idCliente,
        UUID idCorretor,
        StatusAtendimentoEnum status,
        String observacoes,
        LocalDateTime dataCadastro,
        @Schema(description = "Data em que o atendimento foi finalizado. Nulo quando o atendimento ainda está ativo.")
        LocalDateTime dataFim,
        LocalDateTime dataAtualizacao
) {
}
