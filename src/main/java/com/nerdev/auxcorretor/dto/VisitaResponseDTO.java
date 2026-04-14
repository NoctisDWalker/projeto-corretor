package com.nerdev.auxcorretor.dto;

import com.nerdev.auxcorretor.model.enums.InteresseClienteEnum;
import com.nerdev.auxcorretor.model.enums.StatusVisitaEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record VisitaResponseDTO(
        UUID id,
        UUID idAtendimento,
        UUID idImovel,
        LocalDateTime dataHoraAgenda,
        LocalDateTime dataHoraRealizada,
        StatusVisitaEnum statusVisita,
        InteresseClienteEnum interesseCliente,
        String observacoes,
        LocalDate dataCadastro,
        LocalDate dataAtualizacao
) {
}
