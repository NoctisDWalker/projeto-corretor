package com.nerdev.auxcorretor.dto.atendimento;

import com.nerdev.auxcorretor.dto.cliente.ClienteResumoDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorResumoDTO;
import com.nerdev.auxcorretor.model.enums.StatusAtendimentoEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema
public record AtendimentoResumoDTO(
        UUID id,
        CorretorResumoDTO corretor,
        ClienteResumoDTO cliente,
        StatusAtendimentoEnum status
) {
}
