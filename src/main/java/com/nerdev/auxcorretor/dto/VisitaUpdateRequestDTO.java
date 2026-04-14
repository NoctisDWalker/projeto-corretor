package com.nerdev.auxcorretor.dto;

import com.nerdev.auxcorretor.model.enums.InteresseClienteEnum;
import com.nerdev.auxcorretor.model.enums.StatusVisitaEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema
public record VisitaUpdateRequestDTO(
        LocalDateTime dataHoraAgenda,
        LocalDateTime dataHoraRealizada,
        StatusVisitaEnum statusVisita,
        InteresseClienteEnum interesseCliente,
        String observacoes
) {

}
