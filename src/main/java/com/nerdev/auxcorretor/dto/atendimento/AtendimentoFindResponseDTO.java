package com.nerdev.auxcorretor.dto.atendimento;

import com.nerdev.auxcorretor.model.enums.StatusAtendimentoEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema
public record AtendimentoFindResponseDTO(
        UUID id,
        UUID idCliente,
        UUID idCorretor,
        StatusAtendimentoEnum statusAtendimento,
        String observacoes,
        LocalDateTime dataCadastro,
        LocalDateTime dataFim,
        LocalDateTime dataAtualizacao
) {
}
