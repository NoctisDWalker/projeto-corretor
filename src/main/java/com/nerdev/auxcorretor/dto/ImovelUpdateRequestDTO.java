package com.nerdev.auxcorretor.dto;

import com.nerdev.auxcorretor.model.enums.FinalidadeImovelEnum;
import com.nerdev.auxcorretor.model.enums.StatusImovelEnum;
import com.nerdev.auxcorretor.model.enums.TipoImovelEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema
public record ImovelUpdateRequestDTO(
        String titulo,
        String descricao,
        BigDecimal valor,
        FinalidadeImovelEnum finalidadeImovel,
        TipoImovelEnum tipoImovel,
        String cidade,
        String bairro,
        StatusImovelEnum statusImovel
) {

}
