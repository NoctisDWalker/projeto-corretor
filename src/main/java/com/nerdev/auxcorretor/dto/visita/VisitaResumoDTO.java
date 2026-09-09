package com.nerdev.auxcorretor.dto.visita;

import com.nerdev.auxcorretor.dto.imovel.ImovelResumoDTO;
import com.nerdev.auxcorretor.model.enums.InteresseClienteEnum;
import com.nerdev.auxcorretor.model.enums.StatusVisitaEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record VisitaResumoDTO(
        UUID id,
        ImovelResumoDTO imovelResumoDTO,
        StatusVisitaEnum statusVisita,
        InteresseClienteEnum interesseCliente
) {
}
