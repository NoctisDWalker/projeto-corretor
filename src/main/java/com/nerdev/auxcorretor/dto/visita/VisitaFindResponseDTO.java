package com.nerdev.auxcorretor.dto.visita;

import com.nerdev.auxcorretor.model.enums.InteresseClienteEnum;
import com.nerdev.auxcorretor.model.enums.StatusVisitaEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record VisitaFindResponseDTO(
        UUID id,
        UUID idAtendimento,
        UUID idImovel,
        LocalDateTime dataHoraAgendada,
        LocalDateTime dataHoraRealizada,
        StatusVisitaEnum statusVisita,
        InteresseClienteEnum interesseCliente,
        String observacoes,
        LocalDate dataCadastro,
        LocalDate dataAtualizacao
) {
}
