package com.nerdev.auxcorretor.dto.imovel;

import com.nerdev.auxcorretor.model.enums.FinalidadeImovelEnum;
import com.nerdev.auxcorretor.model.enums.StatusImovelEnum;
import com.nerdev.auxcorretor.model.enums.TipoImovelEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema
public record ImovelResumoDTO(
        UUID id,
        String titulo,
        BigDecimal valor,
        FinalidadeImovelEnum finalidadeImovel,
        TipoImovelEnum tipoImovel,
        StatusImovelEnum statusImovel
) {
}
